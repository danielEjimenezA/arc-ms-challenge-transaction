package com.pichincha.services.configuration.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ApiException
{
    private HttpStatus status;
    
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy hh:mm:ss"
    )
    private LocalDateTime timestamp;
    
    private String message;
    private String debugMessage;
    private List<ApiSubException> subErrors = new ArrayList<>(0);
    
    private ApiException()
    {
        timestamp = LocalDateTime.now();
    }
    
    public ApiException(HttpStatus status)
    {
        this();
        this.status = status;
    }
    
    public ApiException(
            HttpStatus status,
            Throwable ex
    )
    {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }
    
    public ApiException(
            HttpStatus status,
            String message,
            Throwable ex
    )
    {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}
