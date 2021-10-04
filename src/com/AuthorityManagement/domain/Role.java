package com.AuthorityManagement.domain;

public class Role {
    private Integer rid ;
    private String  rname ;
    private String rdescription;
    private String reserve1 ;
    private String reserve2 ;

    public Role() {
    }

    public Role(Integer rid, String rname, String rdescription, String reserve1, String reserve2) {
        this.rid = rid;
        this.rname = rname;
        this.rdescription = rdescription;
        this.reserve1 = reserve1;
        this.reserve2 = reserve2;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRdescription() {
        return rdescription;
    }

    public void setRdescription(String rdescription) {
        this.rdescription = rdescription;
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
