package com.AuthorityManagement.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.AuthorityManagement.domain.User;

@WebFilter(urlPatterns = "/*")
public class LoginFilter extends HttpFilter{

	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String path = request.getServletPath();
//		System.out.println(path);
		//不需要认证的  path.split("\\.")[1].equals("js")
		if(path.contains("generatcheckcode") || path.contains("index") || path.contains("login") || path.endsWith("js") || path.endsWith("jpg") || path.endsWith("css")) {
			chain.doFilter(request, response);
			return ;
		}
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) {
			//需要认证，但发现还没有登录
			response.sendRedirect("/AuthorityManagement/");
		}else {
			//认证通过
			chain.doFilter(request, response);
		}
	}
}
