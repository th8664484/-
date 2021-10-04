package com.AuthorityManagement.domain;

public class User {
	private Integer uid;
	private String uname;
	private String upass;
	private String reserve1;
	private String reserve2;
	
	public User() {}
	public User(Integer uid, String uname, String upass, String reserve1, String reserve2) {
		this.uid = uid;
		this.uname = uname;
		this.upass = upass;
		this.reserve1 = reserve1;
		this.reserve2 = reserve2;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpass() {
		return upass;
	}
	public void setUpass(String upass) {
		this.upass = upass;
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
	
	
}
