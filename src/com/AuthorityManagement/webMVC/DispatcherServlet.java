package com.AuthorityManagement.webMVC;

import com.AuthorityManagement.util.Chain;
import com.alibaba.fastjson.JSON;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import com.AuthorityManagement.webMVC.annotation.Controller;
import com.AuthorityManagement.webMVC.annotation.RequestMapping;
import com.AuthorityManagement.webMVC.annotation.RequestParam;
import com.AuthorityManagement.webMVC.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;


public class DispatcherServlet extends HttpServlet {
    //key = 请求  value = MappingInfo对象
    Map<String,MappingInfo> mappingInfoMap = new HashMap<>();
    /*
     * 单实例存储controller对象
     */
    Map<String,Object> controllerMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        //目前配置信息来自俩个部分，而且两个部分都有可能存在
        String xmlPath = super.getInitParameter("classpath");
        if(xmlPath != null && !"".equals(xmlPath)){
            //指定了配置文件，需要读取配置文件
            readXml(xmlPath);
        }

        String packagePath = super.getInitParameter("controller-scan");
        if(packagePath != null && !"".equals(packagePath)){
            //指定了包路径，需要读取包下类中的注解信息
            try {
                readAnnotation(packagePath);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    //读取xml配置文件
    private void readXml(String path) {
        path = Thread.currentThread().getContextClassLoader().getResource(path).getPath();
//        System.out.println(path);
        try {
            InputStream is = new FileInputStream(path);
            //sax解析xml
            SAXReader reader = new SAXReader();
            Document document = reader.read(is);
            //解析mapping标签
            parseMappingElement(document);

            //解析multipart-encoding
            parseMultipartEncodingElement(document);

            //解析mvc-interceptor
            parseMVCInterceptorElement(document);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<InterceptorInfo> interceptorInfoList = new ArrayList<>();
    private void parseMVCInterceptorElement(Document document) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        List<Node> mvcInterceptorElements =  document.selectNodes("mvc/mvc-interceptor") ;
        //遍历节点集合
        for(Iterator<Node> mvcInterceptorNode = mvcInterceptorElements.iterator(); mvcInterceptorNode.hasNext();){
            //Node类型的元素转换为Element类型
            Element mvcInterceptorElement = (Element)mvcInterceptorNode.next();

            InterceptorInfo interceptorInfo = new InterceptorInfo();
            String className = mvcInterceptorElement.attributeValue("class");
            //反射创建目标对象
            Object interceptor = Class.forName(className).newInstance();
            interceptorInfo.setInterceptor(interceptor);

            Element includeElement = (Element) mvcInterceptorElement.selectSingleNode("include");
            if (includeElement != null){
                String includeStr = includeElement.getText();
                interceptorInfo.setIncludeUrl(includeStr);
            }
            Element excludeElement = (Element) mvcInterceptorElement.selectSingleNode("exclude");
            if (excludeElement != null){
                String excludeStr = excludeElement.getText();
                interceptorInfo.setExcludeUrl(excludeStr);
            }

            interceptorInfoList.add(interceptorInfo);
        }

    }



    private String multipartEncoding;
    //读取multipart-encoding标签
    private void parseMultipartEncodingElement(Document document){
        Element multipartEncodingElement = (Element) document.selectSingleNode("mvc/multipart-encoding");
        if (multipartEncodingElement == null) return ;
        multipartEncoding = multipartEncodingElement.getText();
    }
    //读取xml中的<mapping>请求映射信息
    private void parseMappingElement(Document document) throws Exception {

        List<Node> mappingElements =  document.selectNodes("mvc/mapping") ;
        //遍历节点集合
        for(Iterator<Node> mappingNode = mappingElements.iterator(); mappingNode.hasNext();){
            //Node类型的元素转换为Element类型
            Element mappingElement = (Element)mappingNode.next();

            String path = mappingElement.attributeValue("path");
            String classname = mappingElement.attributeValue("class");
            String methodname = mappingElement.attributeValue("method");
            //反射创建目标对象
            Class clazz = Class.forName(classname);
            Object controller = getStringController(classname);
            //通过反射，根据方法名获得方法对象
            //获取mapping标签的子标签<type> 存储目标方法对应的参数列表
            List<Node> typeElements = mappingElement.selectNodes("type");
            //创建一个与配置文件中<type>数量相同的Class数组，装载目标方法的参数列表类型
            Class[] types = new Class[typeElements.size()];
            int i = 0;
            for(Iterator<Node>  typeNode= typeElements.iterator(); typeNode.hasNext();){
                Element typeElement = (Element)typeNode.next();
                String typeStr = typeElement.getText();//获取标签内容
                types[i++] = castStringToClass(typeStr);
            }
            Method method = clazz.getMethod(methodname,types);

            MappingInfo mappingInfo = new MappingInfo(path,controller,method);
            mappingInfoMap.put(path,mappingInfo);
        }

    }
    //将string 变成 对应的.class 对象返回
    private  Class castStringToClass(String typeStr) throws ClassNotFoundException {
        if("int".equals(typeStr)){
            return int.class;
        }
        if("long".equals(typeStr)){
            return long.class;
        }
        if("double".equals(typeStr)){
            return double.class;
        }
        if("float".equals(typeStr)){
            return float.class;
        }
        return  Class.forName(typeStr);
    }

    private Object getStringController(String classname) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
//        Class clazz = Class.forName(classname);
        Object controller  = controllerMap.get(classname);
        if (controller == null){
            controller = Class.forName(classname).newInstance();
            controllerMap.put(classname,controller);
        }
        return controller;
    }
    //读取注解
    private void readAnnotation(String packagePath) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //packagePath = "com.controller,com.controller2..."
        //需要框架从指定的2个包中找到请求映射的注解
        String[] packagePathArray = packagePath.split(",");
        for(String ppath : packagePathArray){
            //包名+类名 = 路径  通过 io 从文件夹中获的文件名，去掉后缀
            String dirPath = ppath.replace(".","/");
            //获取路径D:\IDEA\WEBFrame\out\artifacts\WEBFrame_war_exploded\WEB-INF\classes\com\controller
            try{
                dirPath = Thread.currentThread().getContextClassLoader().getResource(dirPath).getPath();
            }catch (NullPointerException e){
                System.out.println("logging : package path not found["+dirPath+"]");
               // throw  new FileNotFoundException(dirPath);
                continue;
            }

            File dir = new File(dirPath);
            //获得文件夹中的所有子文件的名字
            String[] fnames = dir.list();
            for (String fname:fnames){
                //去掉“.后缀”
                if (fname.endsWith(".class")){
                    String className = fname.substring(0,fname.indexOf("."));
                    //完整的类路径
                    className = ppath+"."+className;
                    //反射
                    Class clazz = Class.forName(className);
                    Controller c = (Controller) clazz.getAnnotation(Controller.class);
                    if(c == null){
                        //当前包中的这个类没有设置@Controller,类中没有请求映射信息
                        continue;
                    }
                    //有@Controller,类中有请求映射关系  遍历类中的方法，找到@RequestMapping
                    Method[] methods = clazz.getMethods();
                    for (Method method:methods){
                        RequestMapping rm = method.getAnnotation(RequestMapping.class);

                        if (rm == null) continue;

                        //方法中有RequestMapping，表示是一个请求映射的方法
                        String path = rm.value();
                        Object controller  = getStringController(className);

                        MappingInfo info = new MappingInfo(path,controller,method);
//                    Object obj = mappingInfoMap.get(path);
//                    if (obj != null){
//                        //当前映射关系已经存在
//                        //去重 continue  覆盖 map.put 抛异常 throw new Exception
//                    }
                        mappingInfoMap.put(path,info);
                    }
                }

            }
        }

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //请求映射，根据请求调用controller的具体方法

        //获得请求 /test1
        //request.getRequestURL();//
        String uri = request.getRequestURI();// /web-demo/test1
        String root = request.getContextPath();// /web-demo
        String path = uri.replace(root,"");

        //根据请求找到对应的mappinginfo 映射信息
        MappingInfo info = mappingInfoMap.get(path);
        if (info == null){
            //没有找到匹配映射信息
            //有可能请求的是一些文件资源 静态
            handleStaticResource(path,request,response);

        }else {

            try {
                handleDynamicResource(info,request,response);
            } catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
        }

    }
    /* 动态资源 */
    private void handleDynamicResource(MappingInfo info,HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, InstantiationException, IOException, ServletException {
        //使用责任链模式 实现切面与目标 调用
        List<InterceptorInfo> cloneInterceptorInfoList = new ArrayList<>();
        cloneInterceptorInfoList.addAll(interceptorInfoList) ;
        Chain chain = new Chain(cloneInterceptorInfoList,info,request,response,multipartEncoding);
        Object result = chain.execute();
       
        handleResponse(result,info.getMethod(),request,response);

    }
    private void handleResponse(Object result,Method method,HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (result == null){//不需要框架响应
            return ;
        }
        ResponseBody rb = method.getAnnotation(ResponseBody.class);
        if (rb != null){
            //有注解 直接响应
            if (result instanceof  String || result instanceof  Integer || result instanceof Long || result instanceof Boolean){
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(result.toString());
            }else {
                //不是简单类型 就是集合对象 需要转换json 响应
                String json = JSON.toJSONString(result);
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(json);

            }
        }else {
            //间接响应
            if (result instanceof  String){
                //不需要携带数据
                String _result = (String) result;
                if (_result.startsWith("redirect:")){
                    //重定向
                    _result = _result.replace("redirect:","");
                    response.sendRedirect(_result);
                }else {
                    request.getRequestDispatcher(_result).forward(request,response);
                }
            }else {
                //是ModelAndView 需要携带数据
                ModelAndView _result = (ModelAndView) result;
                String path = _result.getViewName();
                if (path.startsWith("redirect:")){
                    path = path.replace("redirect:","");
                    path += "?";
                    Set<String> names = _result.getValueNames();
                    for (String name:names){
                        Object value = _result.getObject(name);
                        path += name+"="+value+"&";
                    }
                    response.sendRedirect(path);
                }else {
                    //转发携带数据
                    Set<String> names = _result.getValueNames();
                    for (String name:names){
                        Object value = _result.getObject(name);
                        request.setAttribute(name,value);
                    }
                    request.getRequestDispatcher(path).forward(request,response);
                }
            }
        }
    }







    /*
      处理静态资源，在mapping映射信息中没有找到就表示静态资源
    */
    private  void  handleStaticResource(String path,HttpServletRequest request,HttpServletResponse response) throws IOException {
        if (path.equals("/")){
            path = request.getServletPath();// /index.html
        }
        //根据请求的相对路径，找到对应文件在服务器中的绝对路径
        //Thread.currentThread...()获得classes目录下的文件路径
        //request.getServletContext().realPath() 获得服务器目录下的文件路径
        String ppath = request.getServletContext().getRealPath(path);
        File file = new File(ppath);
        if (!file.exists()){
            //文件不存在
            response.sendError(404,"["+path+"]");
            return ;
        }
        InputStream is = new FileInputStream(file);
        OutputStream os = response.getOutputStream();
        byte[] b = new byte[1024];
        while (true){
           int len = is.read(b);
           if (len == -1) break;
           os.write(b,0,len);
        }
        os.flush();
        is.close();
    }
}
