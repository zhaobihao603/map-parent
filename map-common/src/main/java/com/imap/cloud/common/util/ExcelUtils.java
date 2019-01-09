package com.imap.cloud.common.util;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class ExcelUtils {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String EXCEL_XLS = "xls";  
    private static final String EXCEL_XLSX = "xlsx";  
	
	/** 
     * 判断Excel的版本,获取Workbook 
     * 如果出现错误：java.lang.RuntimeException: Failed to find end of row/cell records
     * 这个是说，文件没有读到正确的结尾行，一般出现这样的问题，是由于读取Excel是由什么其他程序生成出来的。比如是从MySql导出的，等等。
     * 解决方法，就是用Excel打开，点下保存，关闭就可以了。 
     * @param in 
     * @param filename 
     * @return 
     * @throws IOException 
     */  
    public static Workbook getWorkbok(InputStream in,File file) throws IOException{  
        Workbook wb = null;  
        if(file.getName().endsWith(EXCEL_XLS)){  //Excel 2003  
            wb = new HSSFWorkbook(in);  
        }else if(file.getName().endsWith(EXCEL_XLSX)){  // Excel 2007/2010  
            wb = new XSSFWorkbook(in);  
        }  
        return wb;  
    } 
    
    /** 
     * 判断文件是否是excel 
     * @throws Exception  
     */  
    public static void checkExcelVaild(File file) throws Exception{  
        if(!file.exists()){  
            throw new Exception("文件不存在");  
        }  
        if(!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))){  
            throw new Exception("文件不是Excel");  
        }  
    }
    
    /**
     * 读取Excel，反射构造Java bean.excel文件表头必须与java bean的属性名一致
     * @param file Excel文件
     * @param tClass 目标java类
     * @return
     * @throws Exception
     */
    public static <T_T> List<T_T> readFile(File file,Class<T_T> tClass) throws Exception{
    	List<T_T> list = new ArrayList<>();
    	Field[] fields = tClass.getDeclaredFields();
    	
    	checkExcelVaild(file);
    	FileInputStream is = new FileInputStream(file); // 文件流  
    	Workbook workbook = getWorkbok(is,file);// 创建文档
    	
        // 得到第一张工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 标题数组，后面用到，根据索引去标题名称，通过标题名称去字段名称用到 textToKey
        Row title = sheet.getRow(0);
        //cell为null时，当前行的列数会相应减少，并非真实列数，所以需要用表头行来计算列数，那就要求表头行每列必须非null(须有内容)
        int rowNum = title.getPhysicalNumberOfCells();
        String[] titles = new String[rowNum];
        for (int i = 0; i < title.getPhysicalNumberOfCells(); i++) {
            titles[i] = title.getCell(i).getStringCellValue();
        }
        // 行的遍历
        for (int i=1; i <= sheet.getLastRowNum(); i++) {
        	// 得到行
            Row row = sheet.getRow(i);
            T_T t = tClass.newInstance();
            for(int num = 0;num<rowNum;num++){  
            	Cell cell = row.getCell(num);
            	if(cell == null) continue;
            	//循环bean字段(等同循环表头)  
            	for(Field f : fields){
                    if(titles[num].equals(f.getName())){//表头与对应bean的属性名进行比较匹配  
                    	PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(tClass, f.getName());
                        if(pd!=null){  
                        	Method method = pd.getWriteMethod();
                        	setValue(method, t, f.getName(), cell);
                            break;  
                        }  
                    }  
                }
            }
            list.add(t);
        }
    	return list;
    }
    
    /**
     * 读取Excel，反射构造Java bean
     * @param file Excel文件
     * @param headerMapper 表头与java bean映射的Map
     * eg. map.push("表头字段","name");
     * @param tClass 目标java类
     * @return
     * @throws Exception
     */
    public static <T_T> List<T_T> readFile(File file, Map<String, Object> headerMapper, 
    		Class<T_T> tClass) throws Exception{
    	List<T_T> list = new ArrayList<>();
    	
    	checkExcelVaild(file);
    	FileInputStream is = new FileInputStream(file); // 文件流  
    	Workbook workbook = getWorkbok(is,file);// 创建文档
    	
        // 得到第一张工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 标题数组，后面用到，根据索引去标题名称，通过标题名称去字段名称用到 textToKey
        Row title = sheet.getRow(0);
        //cell为null时，当前行的列数会相应减少，并非真实列数，所以需要用表头行来计算列数，那就要求表头行每列必须非null(须有内容)
        int rowNum = title.getPhysicalNumberOfCells();
        String[] titles = new String[rowNum];
        for (int i = 0; i < title.getPhysicalNumberOfCells(); i++) {
            titles[i] = title.getCell(i).getStringCellValue();
        }
        // 行的遍历
        for (int i=1; i <= sheet.getLastRowNum(); i++) {
        	// 得到行
            Row row = sheet.getRow(i);
            T_T t = tClass.newInstance();
            for(int num = 0;num<rowNum;num++){  
            	Cell cell = row.getCell(num);
            	if(cell == null) continue;
                //循环bean字段(等同循环表头)  
            	for(String header : headerMapper.keySet()){
                    if(titles[num].equals(header)){//表头与对应Map进行比较  
                    	PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(tClass, headerMapper.get(header).toString());
                        if(pd!=null){  
                        	Method method = pd.getWriteMethod();
                        	setValue(method, t, header, cell);
                            break;  
                        }  
                    }  
                }  
            }  
            list.add(t);
        }
    	return list;
    }
    
    @SuppressWarnings("deprecation")
	public static <T_T> T_T setValue(Method method,T_T t,String field,Cell cell){
		SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		String paraType = method.getParameterTypes()[0].getName();//获取set方法的参数
		CellType type = cell.getCellType();
		try{
			if(type == CellType.STRING){
				String value = cell.getStringCellValue();
	            //example "10/26/2016 11:41:46";
	            //粗略匹配
	    		Pattern pattern = Pattern.compile("^\\d{1,2}/\\d{1,2}/\\d{4}(\\s\\d{1,2}:\\d{1,2}:\\d{1,2})$");
        		Pattern pattern2 = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}(\\s\\d{1,2}:\\d{1,2}:\\d{1,2})$");
        		Pattern pattern3 = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$");
	    		if(pattern.matcher(value).matches()){
	    			method.invoke(t,fmt.parse(value));
	            }else if(pattern2.matcher(value).matches()){
	            	method.invoke(t,fmt.parse(value));
	            }else if(pattern3.matcher(value).matches()){
	            	method.invoke(t,fmt.parse(value));
	            }else if(paraType.equals("boolean")||paraType.equals("java.lang.Boolean")){
					value = value.trim().toLowerCase();
					method.invoke(t, value.equals("0")||value.equals("false")?false:true);
				}else if(paraType.equals("int")||paraType.equals("java.lang.Integer")){
	            	method.invoke(t,Integer.valueOf(value));
	            }else
	            	method.invoke(t, value);
			}else if(type == CellType.NUMERIC){
				if(paraType.equals("boolean")||paraType.equals("java.lang.Boolean")){
					Double d = cell.getNumericCellValue();
					method.invoke(t, d==0?false:true);
				}else if (paraType.equals("java.lang.String")) {
					Double d = cell.getNumericCellValue();
	        		method.invoke(t, Double.valueOf(d));
	        	}else if (DateUtil.isCellDateFormatted(cell)) {
	        		Date d = cell.getDateCellValue();
	        		method.invoke(t, d);
	        	}else{
	                // 返回数值类型的值
	                Double d = cell.getNumericCellValue();
	                method.invoke(t, d);
	        	}
			}else if(type == CellType.BOOLEAN){
				// 返回布尔类型的值
                boolean value = cell.getBooleanCellValue();
                method.invoke(t, value);
			}else if(type == CellType.FORMULA){
				cell.setCellType(CellType.STRING);
	            String cellValue = cell.getRichStringCellValue().getString();
	            method.invoke(t, cellValue);
			}else{
				//其他不作处理
			}
		}catch(Exception e){
			System.out.println("字段"+field+"无法正确识别excel列的值，"+field+"的类型是"+paraType);
		}
    	return t;
    }

    /** 
     * 读取Excel测试，兼容 Excel 2003/2007/2010 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
    	// 同时支持Excel 2003、2007 
    	/*
        File excelFile = new File("C:/Users/Administrator/Desktop/bb.xls"); // 创建文件对象  
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("ID", "id");
        map.put("回话id", "sessionId");
        map.put("ip地址", "ip");
        map.put("页面", "page");
        map.put("访问时间", "accessTime");
        map.put("停留时间", "stayTime");
        List<TrackLog> list = readFile(excelFile,map, TrackLog.class);
        System.out.println(list.size());
        */
    	/*File excelFile = new File("C:/Users/Administrator/Desktop/dir.xls"); // 创建文件对象  
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id", "id");
        map.put("名称", "name");
        map.put("显示", "display");
        map.put("ename", "ename");
        map.put("备注", "remark");
        map.put("添加日期", "addDate");
        map.put("deleted", "deleted");
        map.put("顺序", "seq");
        List<LayerDir> list = readFile(excelFile,map, LayerDir.class);
        System.out.println(list.size());*/
    	
    	
    }
}
