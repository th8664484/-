package com.AuthorityManagement.webMVC;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModelAndView {
    private String viewName;
    private Map<String,Object> values = new HashMap<>();

    public ModelAndView() {
    }

    public ModelAndView(String viewName, Map<String, Object> values) {
        this.viewName = viewName;
        this.values = values;
    }
    public  String getViewName(){
        return viewName;
    }
    public void setViewName(String viewName){
        this.viewName = viewName;
    }
    public void addObject(String key ,Object value){
        values.put(key,value);
    }
    public Set<String> getValueNames(){
        return  values.keySet();
    }
    public Object getObject(String key){
        return values.get(key);
    }


}
