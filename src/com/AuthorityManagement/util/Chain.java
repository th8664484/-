package com.AuthorityManagement.util;

import com.AuthorityManagement.webMVC.InterceptorInfo;
import com.AuthorityManagement.webMVC.MappingInfo;
import com.AuthorityManagement.webMVC.MulitpartFile;
import com.AuthorityManagement.webMVC.MvcInterceptor;
import com.AuthorityManagement.webMVC.annotation.RequestParam;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 链对象 负责管理和调用 切面对象 + 目标controller对象
 */
public class Chain {
    private List<InterceptorInfo> aspects;//链 执行的 切面
    private MappingInfo info;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String multipartEncoding;

    public Chain(List<InterceptorInfo> aspects, MappingInfo info, HttpServletRequest request, HttpServletResponse response,String multipartEncoding) {
        this.aspects = aspects;
        this.info = info;
        this.request = request;
        this.response = response;
        this.multipartEncoding = multipartEncoding;
    }

    public Object execute() throws InvocationTargetException, IllegalAccessException, InstantiationException {

        if (aspects.size()>0){
            InterceptorInfo inter_info = aspects.remove(0);
            String path = info.getPath();
            Set<String> excludeUrl = inter_info.getExcludeUrl();
            Set<String> includeUrl = inter_info.getIncludeUrl();
            int flag = -1; // -1不执行 1执行 0异常

            if(includeUrl.size() == 0 && excludeUrl.size() ==0){
                flag = 1 ;
            }
            if (excludeUrl.contains(path)){
                flag = -1;
            }else {
                //不需要排除在外，要执行拦截器
                if (excludeUrl.size()>0){
                    flag = 1;
                }
            }

            if (includeUrl.contains(path)){
                if (flag == -1){
                    flag = 0;
                    throw new RuntimeException("配置文件错误");
                }
                flag = 1;
            }

            if (flag == -1){
                //当前拦截器不执行，继续看看下一个拦截器是否执行,自身递归
                return  this.execute();
            }else {
                //当前拦截器需要执行
                MvcInterceptor mvcInterceptor = (MvcInterceptor) inter_info.getInterceptor();
                boolean b = mvcInterceptor.prevHandle(request,response,info.getMethod());
                if (b == true){

                    Object result = this.execute();//调用下一个拦截器/切面 === chain.doFilter()
                    mvcInterceptor.postHandle(request,response);
                    return result;
                }else {
                    //终止调用
                    return  null;
                }
            }

        }else{
            //获得参数
            Map<String,Object> paramMap = receiveParams(request);
            //处理参数
            Object[] paramValues = handleParam(paramMap,info,request,response);
            //调用mappinginfo中的对象的方法，传递参数
            return  info.getMethod().invoke(info.getController(),paramValues);
        }

    }


    /* 接收请求传递的参数 key=>参数名  value=>属性值*/
    private Map<String,Object> receiveParams(HttpServletRequest request){
        Map<String,Object> paramMap = new HashMap<>();
        //参数有2中可能，普通请求，文件上传请求
        //如果普通请求按照文件上传方式处理，会抛异常，如果文件上传请求按照普通请求处理会得不到参数
        try{
            //假设文件上传方式传递参数
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            //该行会判断是否是文件上传请求，如果不是会抛异常
            List<FileItem> fis = upload.parseRequest(request);
            //确定是一个文件上传方式
            for (FileItem item:fis){
                //拿到一个参数
                if (item.isFormField()){//是一个普通参数
                    String key = item.getFieldName();
                    String value = item.getString(multipartEncoding);
                    String[] values = (String[]) paramMap.get(key);
                    if (values == null){
                        values = new String[]{value};
                        paramMap.put(key,values);
                    }else {
                        values = Arrays.copyOf(values,values.length+1);
                        values[values.length-1] = value;
                        paramMap.put(key,values);
                    }

                }else {//文件参数
                    String key = item.getFieldName();
                    String fileName = item.getName();
                    long size = item.getSize();
                    String contentType = item.getContentType();
                    InputStream inputStream = item.getInputStream();
                    MulitpartFile mulitpartFile = new MulitpartFile(fileName,size,contentType,inputStream);
                    MulitpartFile[] mulitpartFiles = (MulitpartFile[]) paramMap.get(key);
                    if (mulitpartFiles == null){
                        mulitpartFiles = new MulitpartFile[]{mulitpartFile};
                        paramMap.put(key,mulitpartFiles);
                    }else {
                        mulitpartFiles = Arrays.copyOf(mulitpartFiles,mulitpartFiles.length+1);
                        mulitpartFiles[mulitpartFiles.length-1] = mulitpartFile;
                        paramMap.put(key,mulitpartFiles);
                    }

                }
            }

        }catch (FileUploadException e) {
            //出现异常表示不是文件上传方式，再按照普通方式处理
            Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()){
                String name = names.nextElement();
                String[] values = request.getParameterValues(name);
                paramMap.put(name,values);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paramMap;
    }

    /*
        按照请求映射关系要调用的那个目标方法，获得目标方法的参数列表，根据参数列表获得所需要的参数，并将参数组装Object[]中
     */
    private Object[] handleParam(Map<String,Object> paramMap,MappingInfo info,HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Method method = info.getMethod();
        Parameter[] parameters =method.getParameters();
        //创建一个与方法参数列表同长度的数组装载参数数据
        Object[] paramValues = new Object[ parameters.length ];
        int i = 0;
        for (Parameter parameter:parameters){
            RequestParam rp = parameter.getAnnotation(RequestParam.class);
            //参数类型
            Class paramType = parameter.getType();
            if (rp != null){
                //证明有注解 表示需要传递一个参数 请求传递的参数已经处理过了，处理后将请求传递的参数存入集合中，key=参数名 value=[]
                String key = rp.value();
                Object value = paramMap.get(key);
                if (value == null || "".equals(value)){
                    //当前所需要的的参数没有值，默认是null，直接找下一个参数值
                    i++;
                    continue;
                }
                //如果参数值存在，起初只有2种表现形成 String[] , MulitpartFile[]
                paramValues[i++] = castType(value,paramType);
            }else {
                //没有@RequestParam 注解  可能是request reponse session
                if (paramType == HttpServletRequest.class){
                    paramValues[i++] = request;
                }else if (paramType == HttpServletResponse.class){
                    paramValues[i++] = response;
                }else if (paramType == HttpSession.class){
                    paramValues[i++] = request.getSession();
                }else {
                    //是一个domain类型  paramType = domain.class   将此次的多个参数，组成一个对象
                    Object paramObject = paramType.newInstance();
                    //通过反射获得对象的属性名，根据属性名找到相同名的参数赋值
                    //建议通过反射找到Set方法赋值
                    Method[] methods = paramType.getMethods();
                    for (Method m : methods){
                        String mname = m.getName();
                        if (mname.startsWith("set")){
                            //是一个set方法 有对应的属性
                            String key = mname.substring(3);//去掉set
                            key = key.substring(0,1).toLowerCase()+key.substring(1);
                            Object value = paramMap.get(key);
                            if (value == null || "".equals(((String[]) value)[0])){
                                //i++ ;
                                continue;
                            }
                            //根据domain的set方法 找到对应的参数  该参数初始是 [] 类型的 需要转换成domain中属性的类型
                            Class domainParamType = m.getParameterTypes()[0];
                            Object v = castType(value,domainParamType);
                            m.invoke(paramObject,v);
                        }
                    }
                    paramValues[i++] = paramObject;
                }
            }
        }
        return  paramValues;
    }

    /*将原始类型的参数数据，转换成目标方法所需要的类型
   原始类型：String[] , MulipatrFile[]
   目标类型：int,long,double,String,int[],long[],String[],Integer,Integer[],
           MultiparFile , MultipartFile[] */
    private Object castType(Object value,Class paramType){
        if(paramType == String.class){
            String str = ((String[])value)[0] ;
            return str ;
        }
        if(paramType == int.class || paramType==Integer.class){
            String str = ((String[])value)[0] ;
            int num = Integer.parseInt(str) ;
            return num ;
        }

        if(paramType == long.class || paramType==Long.class){
            String str = ((String[])value)[0] ;
            long num = Long.parseLong(str) ;
            return num ;
        }

        if(paramType == double.class || paramType==Double.class){
            String str = ((String[])value)[0] ;
            double num = Double.parseDouble(str) ;
            return  num ;
        }

        if(paramType == String[].class){
            return value ;
        }

        //url?cname=1&cname=2&cname3 =》框架会存储成数组[1,2,3]
        if(paramType == int[].class){
            String[] ss =  (String[])value ;
            int[] nums = new int[ss.length];
            for(int j=0;j< ss.length;j++){
                nums[j] = Integer.parseInt(ss[j]);
            }
            return nums ;
        }

        if(paramType == Integer[].class){
            String[] ss =  (String[])value ;
            Integer[] nums = new Integer[ss.length];
            for(int j=0;j< ss.length;j++){
                nums[j] = Integer.valueOf(ss[j]);
            }
            return nums ;
        }

        if(paramType == MulitpartFile.class){
            return ((MulitpartFile[])value)[0];
        }

        if(paramType == MulitpartFile[].class)
            return value ;

        return null ;
    }

}
