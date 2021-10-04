package com.AuthorityManagement.domain;

/**
 * 自动登录的令牌验证信息
 */
public class Token {
    private String tokenid ; //uuid

    private User user ;

    private String ip ;

    private long start ;//生效时间戳  时间毫秒表示

    private long end ; //失效时间戳   时间毫秒表示

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public Token(String tokenid, User user, String ip, long start, long end) {
        this.tokenid = tokenid;
        this.user = user;
        this.ip = ip;
        this.start = start;
        this.end = end;
    }

    public Token() {
    }
}
