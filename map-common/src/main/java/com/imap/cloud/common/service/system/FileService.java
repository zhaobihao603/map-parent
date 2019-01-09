package com.imap.cloud.common.service.system;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.imap.cloud.common.dto.Pager;
import com.imap.cloud.common.entity.system.SysFile;

public interface FileService {
	/**
	 * 记录上传记录到数据库里
	 * 返回数据库里文件记录的主键id
	 * return
	 */
	public String save(MultipartFile attach,String filePath,String module, 
			String title,String description,HttpServletRequest request);
	
	/**
	 * 修改上传记录
	 * 更新成功返回true
	 * 否则返回false
	 */
	public boolean update(String id,String filePath,String module,String title,String description,Long fileSize);
	
	/**
	 * 上传文件到文件服务器
	 * 返回文件在服务器上的路径
	 * return 
	 */
	public String uploadFile(MultipartFile attach,String module,HttpServletRequest request);
	
	/**
	 * 根据主键获取上传文件
	 * @param file 要上传的文件
	 * @param module 模块
	 * @param userId 用户id
	 * @return
	 */
	public SysFile load(String id);
	/**
	 * 上传文件，并记录到数据库里
	 * 返回数据库里文件记录的主键id
	 * 
	 * @param file 要上传的文件
	 * @param module 模块,比如:Module.SCENIC.code
	 * @param userId 用户id
	 * @return
	 */
	public String upload(MultipartFile attach,String module,HttpServletRequest request);
	/**
	 * 上传多个个文件
	 * @param file 要上传的文件
	 * @param module 模块
	 * @param userId 用户id
	 * @return
	 */
	public String[] upload(MultipartFile[] file,String module,HttpServletRequest request);
	
	/**
	 * 下载文件
	 * @param id，文件记录表主键id
	 * @return
	 */
	public ResponseEntity<byte[]> download(String id,HttpServletRequest request) throws Exception;

	/**
	 * 获取校园风景的图片
	 * @param id，主键id
	 * @return
	 */
	public List<SysFile> getScenicPhoto(String scenicId);

	/**
	 * 根据模块名称获取文件记录
	 * @param id，主键id
	 * @return
	 */
	public List<SysFile> getByModule(String name);

	public List<SysFile> selectByIds(String[] lst);

	/**
	 * 根据模块和文件类型分页查询
	 * @param conditionMap
	 * @param fileFormats 
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public Pager<SysFile> search(Map conditionMap,String[] fileFormats, Integer pageSize, Integer pageNum);
	
	/**
     * 把VR压缩包解压到具体位置
     * @param path 文件路径
     * @param dir  指定目录，默认为根目录下的vr
     * @throws Exception
     */
	public void unzipVR(String path,String dir,HttpServletRequest request)throws Exception;
}
