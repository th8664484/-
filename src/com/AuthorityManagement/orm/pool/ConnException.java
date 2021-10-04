package com.AuthorityManagement.orm.pool;

public class ConnException extends RuntimeException{
    public ConnException(){}
    public ConnException(String mess){
        super(mess);
    }
}
