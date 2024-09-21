package com.ma.isw.bookcafe.model.dao.exception;

public class NoEventFoundException extends Exception
{
    public NoEventFoundException(String reason) {
        super(reason);
    }
}