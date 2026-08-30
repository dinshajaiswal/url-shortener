package com.url_shortener.demo.exception;

public class ShortCodeNotFoundException extends RuntimeException{
    public ShortCodeNotFoundException(String message){
        super(message);
    }
}
