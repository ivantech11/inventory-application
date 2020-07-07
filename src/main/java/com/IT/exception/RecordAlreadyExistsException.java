package com.IT.exception;

public class RecordAlreadyExistsException extends RuntimeException{
    public RecordAlreadyExistsException(String name, String resource){
        super(String.format("%s already exists in %s",name,resource));
    }
}