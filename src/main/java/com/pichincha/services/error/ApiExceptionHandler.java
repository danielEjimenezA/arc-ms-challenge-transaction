package com.pichincha.services.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler
{
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e)
    {
        ApiException apiException = new ApiException(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                e
        );
        return new ResponseEntity<>(
                apiException,
                HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(NotFoundException e)
    {
        log.error(e.toString());
        return buildResponseEntity(new ApiException(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                e
        ));
    }
    
    @ExceptionHandler(
            {
                    DataIntegrityViolationException.class,
                    NoSuchElementException.class
            }
    )
    public ResponseEntity<Object> dataIntegrityViolationException(Exception e)
    {
        log.error(e.toString());
        return buildResponseEntity(new ApiException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                e
        ));
    }
    
    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> emptyResultDataAccessException(Exception e)
    {
        log.error(e.toString());
        return buildResponseEntity(new ApiException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                e
        ));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException e)
    {
        log.error(e.toString());
        ApiException apiError = new ApiException(
                HttpStatus.BAD_REQUEST,
                "An error occurred with parameter validation.",
                e
        );
        e
                .getConstraintViolations()
                .forEach(element -> apiError
                        .getSubErrors()
                        .add(new ApiSubException(
                                element.getInvalidValue(),
                                element.getMessage()
                        )));
        return buildResponseEntity(apiError);
    }
    
    @ExceptionHandler(
            {
                    MethodArgumentNotValidException.class,
                    BindException.class
            }
    )
    public ResponseEntity<Object> bindException(BindException e)
    {
        log.error(e.toString());
        ApiException apiError = new ApiException(
                HttpStatus.BAD_REQUEST,
                "Error in DTO validation.",
                e
        );
        e
                .getBindingResult()
                .getAllErrors()
                .forEach(element -> apiError
                        .getSubErrors()
                        .add(new ApiSubException(
                                element.getArguments(),
                                element.getDefaultMessage()
                        )));
        return buildResponseEntity(apiError);
    }
    
    private ResponseEntity<Object> buildResponseEntity(ApiException apiException)
    {
        return new ResponseEntity<>(
                apiException,
                apiException.getStatus()
        );
    }
    
}
