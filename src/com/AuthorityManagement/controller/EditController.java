package com.AuthorityManagement.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.AuthorityManagement.domain.User;
import com.AuthorityManagement.service.UserService;
import com.AuthorityManagement.util.MySpring;


@WebServlet( urlPatterns = "/edit")
public class EditController extends HttpServlet{

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService service = MySpring.getBean("com.AuthorityManagement.service.UserService");
		String uid = request.getParameter("uid");
		User user = service.select(uid);
//		System.out.println(user.getUname()+"-"+user.getUpass());
		
		String [][] str = new String[][] {
			{"ID","uid", user.getUid().toString()},
			{"姓名","uname", user.getUname()},
			{"密码","upass", user.getUpass()},
			{"备注1","reserve1", user.getReserve1()},
			{"备注2","reserve2", user.getReserve2()}
		};		
		request.setAttribute("str", str);
		request.setAttribute("page", request.getParameter("page"));
		request.setAttribute("rows", request.getParameter("rows"));
		request.getRequestDispatcher("edit.jsp").forward(request, response);
	}
}
