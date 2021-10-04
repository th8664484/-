package com.AuthorityManagement.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.AuthorityManagement.domain.User;
import com.AuthorityManagement.service.UserService;
import com.AuthorityManagement.util.MySpring;

@WebServlet(urlPatterns = "/userimport")
public class UserIimportController extends HttpServlet{

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		UserService service = MySpring.getBean("com.AuthorityManagement.service.UserService");
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024*1024*1024);
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			List<FileItem> itemList = upload.parseRequest(request);
			FileItem item = itemList.get(0);
			String fileName = item.getName();
			String value = "请上传.xlsx类型的文件";
			if(this.judge(fileName)) {
//				System.out.println(fileName);
				InputStream is = item.getInputStream();
				
				Workbook book = WorkbookFactory.create(is);//把Exel内容读取出来
				Sheet sheet = book.getSheetAt(0);//获取第一个工作簿
				for(int i=1;i<sheet.getLastRowNum();i++) {
					Row row = sheet.getRow(i);//行
					Cell c1 = row.getCell(0);//每行的第一个单元格
					Cell c2 = row.getCell(1);//每行的第二个单元格
					String name = c1.toString();//获取值
					String password = c2.toString().replace(".0", "");
					User user = new User(null,name,password,null,null);
					
					service.save(user);				
				}
					value = "保存成功";
			}
			request.setAttribute("value", value);			
			request.getRequestDispatcher("userImport.jsp").forward(request, response);
		} catch (FileUploadException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	private boolean judge(String fileName) {
		String[] str = fileName.split("\\.");
		return str[1].equals("xlsx")?true:false;
	}
}
