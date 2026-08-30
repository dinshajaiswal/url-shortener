package com.url_shortener.demo.exception;

public class InvalidUrlException extends RuntimeException{
    public InvalidUrlException(String message){
        super(message);
    }
}
