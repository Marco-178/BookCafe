package com.ma.isw.bookcafe.model.dao.exception;

public class NoBookFoundException extends Exception {
    public NoBookFoundException(String reason) {
        super(reason);
    }
}
