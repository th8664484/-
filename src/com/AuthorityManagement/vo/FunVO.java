package com.AuthorityManagement.vo;

import java.util.List;

public class FunVO {
    private String title;
    private Integer id;
    private List<FunVO> children;

    public FunVO() {
    }

    public FunVO(String title, Integer id, List<FunVO> children) {
        this.title = title;
        this.id = id;
        this.children = children;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<FunVO> getChildren() {
        return children;
    }

    public void setChildren(List<FunVO> children) {
        this.children = children;
    }
}
