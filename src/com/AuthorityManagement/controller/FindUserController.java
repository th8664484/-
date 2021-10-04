package com.AuthorityManagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.AuthorityManagement.domain.PageInfo;
import com.AuthorityManagement.service.UserService;
import com.AuthorityManagement.util.MySpring;

@WebServlet(urlPatterns = "/findUser")
public class FindUserController extends HttpServlet{

	 protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        //查询用户信息，去展示
	        int page = Integer.parseInt( request.getParameter("page") ) ;
	        int rows = Integer.parseInt( request.getParameter("rows") ) ;
	        UserService service = MySpring.getBean("com.AuthorityManagement.service.UserService");
	        PageInfo info = service.findUserByPage(page,rows);
	        
	        request.setAttribute("info",info);
	        request.getRequestDispatcher("users.jsp").forward(request,response);
	    }
}
