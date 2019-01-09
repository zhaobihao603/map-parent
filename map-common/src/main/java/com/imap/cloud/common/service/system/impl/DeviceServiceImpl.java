package com.imap.cloud.common.service.system.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.dao.system.DeviceMapper;
import com.imap.cloud.common.dto.DeviceDTO;
import com.imap.cloud.common.entity.system.Device;
import com.imap.cloud.common.enums.Module;
import com.imap.cloud.common.listener.MapInitializationAfterStartupProcessor;
import com.imap.cloud.common.service.system.DeviceService;
import com.imap.cloud.common.service.system.FileService;

@LogDescription(name="设备管理服务")
@Service
public class DeviceServiceImpl implements DeviceService {
	final static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private FileService fileService;
	
	@Override
	public List<Device> selectByParentType(String parentType) {
		return deviceMapper.selectByParentType(parentType);
	}

	@Override
	public int countByParentType(String parentType) {
		return deviceMapper.countByParentType(parentType);
	}

	@Override
	public void updateImage(HttpServletRequest request) {
		//创建一个通用的多部分解析器.  
		CommonsMultipartResolver commonsMultipartResolver = new   
				CommonsMultipartResolver(request.getSession().getServletContext());  
		//设置编码  
		commonsMultipartResolver.setDefaultEncoding("utf-8");  
		//判断 request 是否有文件上传,即多部分请求...  
		if (commonsMultipartResolver.isMultipart(request)) { 
			//转换成多部分request  
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
			MultipartFile multipartFile = multipartRequest.getFile("fileUpload");
			String picUrl = "";
			if(multipartFile != null){
				String path = fileService.upload(multipartFile, Module.SYSTEM.getCode(), request);
				//保存imageid
				String id = request.getParameter("id");
				DeviceDTO deviceDTO = new DeviceDTO();
				deviceDTO.setId(id);
				deviceDTO.setImageId(path);
				deviceMapper.updateById(deviceDTO);
				DeviceDTO deviceDTO1 = new DeviceDTO();
				deviceDTO1.setId(id);
				List<Device> dlist =deviceMapper.selectById(deviceDTO1);
				picUrl = dlist.get(0).getImage().getFileUrl();
			}
			
		}
		
	}

	@Override
	public List<Device> selectByConditionMap(DeviceDTO deviceDTO) {
		List<Device> devices =deviceMapper.selectByConditionMap(deviceDTO);
		return devices;
	}

	@Override
	public int countByConditionMap(DeviceDTO deviceDTO) {
		return deviceMapper.countByConditionMap(deviceDTO);
	}

	@Override
	public List<String> comboboxType(String parentType) {
		return deviceMapper.comboboxType(parentType);
	}

	@Override
	public void deleteById(String id) {
		deviceMapper.deleteById(id);
	}

	@Override
	public void insert(DeviceDTO deviceDTO) {
		deviceMapper.insert(deviceDTO);
	}

	@Override
	public void updateById(DeviceDTO deviceDTO) {
		deviceMapper.updateById(deviceDTO);
		
	}

	@Override
	public Map<Object, Object> treeData() {
		this.removeInCss();
		List<Device> dlist = deviceMapper.selectIsStatus();
		Map<Object,Object> rtMap = new HashMap<Object,Object>();
		List<Map> rtList = new ArrayList<Map>();
		List<Device> secList = new ArrayList<Device>();
		//根部
		for (Device device : dlist) {
			String status = device.getStatus();
			if(status!= null && status.equals("-1")){
				rtMap.put("id", device.getId());
				rtMap.put("text", device.getName());
				rtMap.put("status", "-1");
				//rtMap.put("imgUrl", device.getImage().getFileUrl());
			}
			if(status!= null && status.equals("0")){
				Map map = new HashMap();
				map.put("id", device.getId());
				map.put("text", device.getName());
				map.put("status", "0");
				if(device.getImage()!= null){
					String picUrl = device.getImage().getFileUrl();
					String icon = this.writeInCss(picUrl);
					map.put("iconCls", icon);
					map.put("imgUrl", picUrl);
				}
				rtList.add(map);
			}
			if(status!= null && status.equals("1")){
				secList.add(device);
			}
		}
		for(Map map :rtList){
			String id = map.get("id").toString();
			List _list = new ArrayList();
			for(Device device :secList){
				String parentid = device.getParent().getId();
				if(parentid.equals(id)){
					Map _map = new HashMap();
					_map.put("id", device.getId());
					_map.put("text", device.getName());
					_map.put("status", "1");
					if(device.getImage()!= null){
						_map.put("imgUrl", device.getImage().getFileUrl());
						String icon = this.writeInCss(device.getImage().getFileUrl());
						_map.put("iconCls", icon);
					}
					_list.add(_map);
				}
			}
			map.put("children", _list);
		}
		rtMap.put("children", rtList);
		return rtMap;
	}

	@Override
	public void deleteByParentId(String parentid) {
		deviceMapper.deleteByParentId(parentid);
		deviceMapper.deleteById(parentid);
	}

	@Override
	public String writeInCss(String picUrl) {
		BufferedWriter writer  = null;
		BufferedReader reader = null;
		String classPath = MapInitializationAfterStartupProcessor.class.getResource("/").getFile();
		String _path = classPath.substring(0, classPath.indexOf("WEB-INF"));
    	String filePath = new StringBuffer(_path).append("resource/easyui/icon.css").toString();
    	File file = new File(filePath);
    	String name = formatter.format(new Date());
		Random random = new Random();
        int rannum = (int) (random.nextDouble() * 900) + 100;// 获取3位随机数
        String cssName = new StringBuffer("iconl-").append(name).append(rannum).toString();
    	try {
			String content ="";
			InputStreamReader readerStream = new InputStreamReader(new FileInputStream(filePath),"UTF-8");
			reader = new BufferedReader(readerStream);
			String line = null;  
	        while ((line = reader.readLine()) != null) {  
	            content += line + "\n";  
	        }  
	        OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
			writer  = new BufferedWriter(writerStream);
			writer.write(content);
			writer.write("."+cssName+"{");
			writer.write("\n");
			writer.write("background:url('"+picUrl+"') no-repeat center center;");
			writer.write("\n");
			writer.write("}");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
            try {
                writer.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		return cssName;
	}

	@Override
	public String removeInCss() {
		BufferedWriter writer  = null;
		BufferedReader reader = null;
		try {
			String classPath = MapInitializationAfterStartupProcessor.class.getResource("/").getFile();
			String _path = classPath.substring(0, classPath.indexOf("WEB-INF"));
	    	String filePath = new StringBuffer(_path).append("resource/easyui/icon.css").toString();
	    	String content ="";
			InputStreamReader readerStream = new InputStreamReader(new FileInputStream(filePath),"UTF-8");
			reader = new BufferedReader(readerStream);
			String line = null;  
	        while ((line = reader.readLine()) != null) { 
	        	if(line.contains(".iconl-")){
	        		break;
	        	}
	            content += line + "\n";  
	        }
	        OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
			writer  = new BufferedWriter(writerStream);
			writer.write(content);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
            try {
            	writer.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		  
		return null;
	}

}
