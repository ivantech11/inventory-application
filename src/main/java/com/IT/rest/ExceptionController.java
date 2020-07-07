package com.IT.rest;

import com.IT.dto.ErrorDto;
import com.IT.exception.RecordAlreadyExistsException;
import com.IT.exception.RecordNotFoundException;
import com.IT.exception.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RecordAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> recordAlreadyExists(RecordAlreadyExistsException recordAlreadyExistsException){
        ErrorDto dto = new ErrorDto(recordAlreadyExistsException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorDto> recordNotFound(RecordNotFoundException recordNotFoundException){
        ErrorDto dto = new ErrorDto(recordNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDto> validationError(ValidationException validationException){
        ErrorDto dto = new ErrorDto(validationException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return handleExceptionInternal(ex, errors, headers, status, request);
    }
}