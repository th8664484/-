package com.AuthorityManagement.webMVC;

import java.lang.reflect.Method;

/**
 * 存储请求映射关系
 */
public class MappingInfo {
    private String path;//请求命令
    private Object controller;//目标对象
    private Method method;//目标方法

    public MappingInfo(){}
    public MappingInfo(String path, Object controller, Method method) {
        this.path = path;
        this.controller = controller;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
