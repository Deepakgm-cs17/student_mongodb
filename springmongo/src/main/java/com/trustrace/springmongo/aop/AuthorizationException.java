package com.trustrace.springmongo.aop;

public class AuthorizationException extends Exception{
    public AuthorizationException(String message){
        super(message);
    }
}
