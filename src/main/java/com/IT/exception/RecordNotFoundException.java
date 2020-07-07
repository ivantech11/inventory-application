package com.IT.exception;

public class RecordNotFoundException extends RuntimeException{
    public RecordNotFoundException(String name, String resource){
        super(String.format("%s is not found in %s",name,resource));
    }
}