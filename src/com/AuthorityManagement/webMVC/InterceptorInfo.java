package com.AuthorityManagement.webMVC;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InterceptorInfo {
    private Object interceptor;
    private Set<String> includeUrl = new HashSet<>();
    private Set<String> excludeUrl = new HashSet<>();

    public InterceptorInfo() {
    }

    public InterceptorInfo(Object interceptor, Set<String> includeUrl, Set<String> excludeUrl) {
        this.interceptor = interceptor;
        this.includeUrl = includeUrl;
        this.excludeUrl = excludeUrl;
    }

    public Object getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(Object interceptor) {
        this.interceptor = interceptor;
    }

    public Set<String> getIncludeUrl() {
        return includeUrl;
    }

    public void setIncludeUrl(String interceptorStr) {
        if (interceptorStr != null && !"".equals(interceptorStr)){
            includeUrl.addAll(Arrays.asList(interceptorStr.split(",")));
        }
    }

    public Set<String> getExcludeUrl() {
        return excludeUrl;
    }

    public void setExcludeUrl(String excludeStr) {
        if (excludeStr != null && !"".equals(excludeStr)){
            excludeUrl.addAll(Arrays.asList(excludeStr.split(",")));
        }
    }
}
