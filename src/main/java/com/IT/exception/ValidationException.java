package com.IT.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(String key, String value){
        super(String.format("Validation error for %s: %s",key,value));
    }
}