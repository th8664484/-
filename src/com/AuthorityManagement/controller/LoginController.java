package com.AuthorityManagement.controller;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.AuthorityManagement.domain.Token;
import com.AuthorityManagement.domain.User;
import com.AuthorityManagement.service.UserService;
import com.AuthorityManagement.util.MySpring;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet{

	private UserService service = new MySpring().getBean("com.AuthorityManagement.service.UserService");
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			String uname = request.getParameter("uname");
			String upass = request.getParameter("upass");
			String autoflag = request.getParameter("autoflag");
			String code = request.getParameter("code");
			if((uname==null || uname.equals("") || (upass==null || upass.equals("")))) {
				request.setAttribute("value", "请输入用户名或密码");
				request.getRequestDispatcher("/").forward(request, response);
				return ;
			}
			if(!code.toUpperCase().equals(request.getSession().getAttribute("code"))) {
				request.setAttribute("value", "验证码错误");
				request.getRequestDispatcher("/").forward(request, response);
				return ;
			}
			
			User user = new UserService().checkLogin(uname, upass);
			if(user != null) {
				 //自动登录处理
				if(autoflag != null && !"".equals(autoflag)) {
					String tokenId = UUID.randomUUID().toString();
					Cookie c = new Cookie("tokenId",tokenId);
					c.setMaxAge(60*60*24);//秒
					response.addCookie(c);
					
					Token token = new Token(
							tokenId,
							user,
							request.getRemoteAddr(),
							System.currentTimeMillis(),
							System.currentTimeMillis()+1000L*60*60*24*7
							);
					request.getServletContext().setAttribute(tokenId, token);
				}
				request.getSession().setAttribute("user", user);
				Map<String,Object> map = service.findUserAuth(user.getUid());
				request.getSession().setAttribute("loginAuth",map);
				response.sendRedirect("main.jsp");
//				request.getRequestDispatcher("main.jsp").forward(request, response);
			}else {
				request.setAttribute("value", "用户名或密码错误");
				request.getRequestDispatcher("/").forward(request, response);
			}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}
}
