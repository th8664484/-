package com.AuthorityManagement.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.AuthorityManagement.service.UserService;
import com.AuthorityManagement.util.MySpring;

@WebServlet(urlPatterns = "/delete")
public class DeleteUserController extends HttpServlet{

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		String uid = request.getParameter("uid");
		UserService service = MySpring.getBean("com.AuthorityManagement.service.UserService");
		String value = service.delete(uid);
		
		
		PrintWriter pw = response.getWriter();
		pw.write("<script>alert('"+value+"'); location.href ='findUser?page=1&rows=10'</script>");
		pw.flush();
//		request.setAttribute("value", value);
//		request.getRequestDispatcher("users.jsp").forward(request, response);
		
	}
}
