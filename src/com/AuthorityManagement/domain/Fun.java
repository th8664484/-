package com.AuthorityManagement.domain;

public class Fun {
	    private Integer fid ;
	    private String fname ;
	    private Integer ftype ;// 1 菜单， 2 按钮
	    private String fhref ;//菜单对应的请求url
	    private String fauth ;//权限范围com.qxgl , com:qxgl
	    private Integer pid ;//父级功能编号，根菜单的父级编号-1  
	    private String reserve1 ;
	    private String reserve2 ;

	    public Integer getFid() {

	        return fid;
	    }

	    public void setFid(Integer fid) {
	        this.fid = fid;
	    }

	    public String getFname() {
	        return fname;
	    }

	    public void setFname(String fname) {
	        this.fname = fname;
	    }

	    public Integer getFtype() {
	        return ftype;
	    }

	    public void setFtype(Integer ftype) {
	        this.ftype = ftype;
	    }

	    public String getFhref() {
	        return fhref;
	    }

	    public void setFhref(String fhref) {
	        this.fhref = fhref;
	    }

	    public Integer getPid() {
	        return pid;
	    }

	    public void setPid(Integer pid) {
	        this.pid = pid;
	    }

	    public String getFauth() {
	        return fauth;
	    }

	    public void setFauth(String fauth) {
	        this.fauth = fauth;
	    }

	    public String getReserve1() {
	        return reserve1;
	    }

	    public void setReserve1(String reserve1) {
	        this.reserve1 = reserve1;
	    }

	    public String getReserve2() {
	        return reserve2;
	    }

	    public void setReserve2(String reserve2) {
	        this.reserve2 = reserve2;
	    }

	    public Fun(Integer fid, String fname, Integer ftype, String fhref, Integer pid, String fauth, String reserve1, String reserve2) {
	        this.fid = fid;
	        this.fname = fname;
	        this.ftype = ftype;
	        this.fhref = fhref;
	        this.pid = pid;
	        this.fauth = fauth;
	        this.reserve1 = reserve1;
	        this.reserve2 = reserve2;
	    }

	    public Fun() {
	    }
	

}
