package com.onebox.exceptions.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.onebox.constants.ErrorCodes;
import com.onebox.exceptions.ExceptionResponse;
import com.onebox.exceptions.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("An unexpected error occur",ex);

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ErrorCodes.INTERNAL_SERVER_ERROR, ex.getMessage());

        request.getDescription(false);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        logger.error("An unexpected error occur",ex);

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ErrorCodes.INTERNAL_SERVER_ERROR, ex.getMessage());

        request.getDescription(false);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Invalid Arguments ", ex);

        ExceptionResponse exceptionResponse =
                new ExceptionResponse(ErrorCodes.INVALID_REQUEST, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        logger.error("MethodArgumentNotValid ", ex);

        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errors = new ArrayList<>();
        fieldErrors.forEach(f ->
                errors.add(String.format("%s : %s",f.getField(),f.getDefaultMessage()))
        );

        ExceptionResponse exceptionResponse =
                new ExceptionResponse(ErrorCodes.VALIDATION_FAILED, errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);

    }
    
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        log.error("RestExceptionHandler.handleCartNotFoundException - error: [{}]", ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

}
