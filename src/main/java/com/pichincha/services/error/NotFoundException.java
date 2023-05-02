package com.pichincha.services.error;

public class NotFoundException extends RuntimeException
{
    public NotFoundException(String msg)
    {
        super(msg);
    }
}
