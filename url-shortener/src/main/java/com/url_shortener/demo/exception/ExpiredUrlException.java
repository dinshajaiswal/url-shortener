package com.url_shortener.demo.exception;

public class ExpiredUrlException extends RuntimeException{
    public ExpiredUrlException(String message){
        super(message);
    }
}
