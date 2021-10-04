package com.AuthorityManagement.webMVC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public interface MvcInterceptor {
    //请求处理之前需要切面执行操作
    public boolean prevHandle(HttpServletRequest request, HttpServletResponse response, Method targetMethod);

    public void postHandle(HttpServletRequest request, HttpServletResponse response);
}
