package com.course1.practicaljava.exception;


public class IllegalApiParamException extends RuntimeException {

    private static final long serialVersionUID = -732530286541996647L;

    public IllegalApiParamException(String s){
        super(s);
    }

}
