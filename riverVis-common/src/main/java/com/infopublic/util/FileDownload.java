package com.infopublic.util;

import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 下载文件
 * @version
 */
public class FileDownload {

	/**
	 * @param response 
	 * @param filePath		//文件完整路径(包括文件名和扩展名)
	 * @param fileName		//下载后看到的文件名
	 * @return  文件名
	 */
	public static String fileDownload(final HttpServletResponse response, String filePath, String fileName) {
		   
		byte[] data;
		
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				PrintWriter out =  response.getWriter();
//				response.setContentType("text/html; charset=utf-8");
				response.setHeader("Content-type","text/html;charset=UTF-8");
				out.print("<script > alert(\"没找到文件或文件已删除!\"); </script>");
//				out.println("没找到文件或文件已删除");
				out.flush();
				out.close();
				return "notfound";
//				throw new FileNotFoundException(filePath);
			}else{
				data = FileUtil.toByteArray2(filePath);
				fileName = URLEncoder.encode(fileName, "UTF-8");  
				response.reset();  
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
				response.addHeader("Content-Length", "" + data.length);  
				response.setContentType("application/octet-stream;charset=UTF-8");  
				OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());  
				outputStream.write(data);  
				outputStream.flush();  
				outputStream.close();
				response.flushBuffer();
				deleteZip(filePath);
			}
		} catch (IOException e) {
			PrintWriter out = null;
			try {
				out = response.getWriter();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			response.setContentType("text/html; charset=utf-8");
			response.setHeader("Content-type","text/html;charset=UTF-8");
			out.println("<script>alert(\"下载出错!\"); </script>");
			out.flush();
			out.close();
//			alert("error",response);
			return "error";
		} 
		return "success";
	} 
	/**
     * 删除zip
     */
    private static void deleteZip(String path) {

        File  file= new File(path);// 里面输入特定目录
            if (file.getName().endsWith("zip"))// 获得文件名，如果后缀为“”，这个你自己写，就删除文件
            {
            	file.delete();// 删除文件
            }
    }
//    /**
//     * 删除文件
//     */
//    public static void deletefile(String path) {
//    	
//    	File  file= new File(path);// 里面输入特定目录
//    	file.delete();// 删除文件
//    }
    /**
     * 将内容导出到excel并保存
     * @param filepath
     * @param filename
     * @param model
     */
    @SuppressWarnings("unchecked")
	public static void exportExcel(String filepath,String filename,Map<String, Object> model) { //filename是生成文件的路径及用户名;
    	// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet;
		HSSFCell cell;
		sheet = workbook.createSheet("sheet1");
		
		List<String> titles = (List<String>) model.get("titles");
		int len = titles.size();
		HSSFCellStyle headerStyle = workbook.createCellStyle(); //标题样式
//		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont headerFont = workbook.createFont();	//标题字体
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short)11);
		headerStyle.setFont(headerFont);
		short width = 20,height=25*20;
		sheet.setDefaultColumnWidth(width);
		 HSSFRow row = sheet.createRow((int) 0);
		 for(int i=0; i<len; i++){ //设置标题
	            cell = row.createCell(i);    
	            cell.setCellValue(titles.get(i));    
	            cell.setCellStyle(headerStyle);    
	            sheet.autoSizeColumn(i);    
	        }    
		 sheet.getRow(0).setHeight(height);
	        
		
		HSSFCellStyle contentStyle = workbook.createCellStyle(); //内容样式
		List<PageData> varList = (List<PageData>) model.get("varList");
		int varCount = varList.size();
		for(int i=0; i<varCount; i++){
			PageData vpd = varList.get(i);
			row = sheet.createRow(i + 1);  
			for(int j=0;j<len;j++){
				String varstr = vpd.getString("var"+(j+1)) != null ? vpd.getString("var"+(j+1)) : "";
				 cell = row.createCell(j);    
		            cell.setCellValue(varstr);    
		            cell.setCellStyle(contentStyle);    
//		            sheet.autoSizeColumn(i);    
			}
			
		}
        try  
        {  
        	FileUtil.createFile(filepath+filename+".xls");
            FileOutputStream fout = new FileOutputStream(filepath+filename+".xls");  
            workbook.write(fout);  
            fout.close();  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
    }
}
