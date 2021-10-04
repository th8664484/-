package com.AuthorityManagement.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.AuthorityManagement.domain.User;
import com.AuthorityManagement.service.UserService;
import com.AuthorityManagement.util.MySpring;

@WebServlet( urlPatterns = "/modifyuser")
public class ModifyUserController extends HttpServlet{

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			UserService service = MySpring.getBean("com.AuthorityManagement.service.UserService");
			String uid = request.getParameter("uid");
			String uname = request.getParameter("uname");
			String upass = request.getParameter("upass");
			String reserve1 = request.getParameter("reserve1");
			String reserve2 = request.getParameter("reserve2");
			String page =request.getParameter("page");
			String rows =request.getParameter("rows");
			reserve2 = reserve2.equals("")? null :reserve2;
		
			User user = new User(Integer.parseInt(uid),uname,upass,reserve1,reserve2);
			String value = service.modify(user);

			PrintWriter pw = response.getWriter();
			pw.write("<script>alert('"+value+"'); location.href ='findUser?page="+page+"&rows="+rows+"'</script>");
			pw.flush();
 	}
}
