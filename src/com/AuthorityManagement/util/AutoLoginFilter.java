package com.AuthorityManagement.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.AuthorityManagement.domain.Token;

@WebFilter(urlPatterns = "/*")
public class AutoLoginFilter extends HttpFilter{
	
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String path = request.getServletPath();
		//不需要自动登录
		if(path.endsWith("js") || path.endsWith("jpg") || path.endsWith("css") || path.contains("generatcheckcode")) {
			chain.doFilter(request, response);
			return ;
		}
		Cookie[] cs = request.getCookies();
		if(cs != null && cs.length>0) {
			for(Cookie c:cs) {
				String name = c.getName();
				String value = c.getValue();
				if("tokenId".equals(name)) {
					Token token =  (Token) request.getServletContext().getAttribute(value);
					//不符合自动登录条件
					if(token == null ||
					   !token.getIp().equals(request.getRemoteAddr()) ||
					   token.getEnd() < System.currentTimeMillis()
							) {
						chain.doFilter(request, response);
						return ;
					}else {
						//符合自动登录条件
						request.getSession().setAttribute("user",token.getUser());
						if(path.contains("index")){
                            //此次请求正要访问登录页，因为已经自动登录，不需要继续访问登录页了，直接进入主页
                            response.sendRedirect("/AuthorityManagement/main.jsp");
                            return ;
                        }

                        chain.doFilter(request,response);
                        return ;
					}
				}
			}
		}
		chain.doFilter(request, response);
		
	}

}
