package com.pichincha.services.configuration.error;

public class NotFoundException extends RuntimeException
{
    public NotFoundException(String msg)
    {
        super(msg);
    }
}
