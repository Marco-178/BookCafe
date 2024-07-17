package com.ma.isw.bookcafe.model.dao.exception;

public class DuplicatedUsernameException extends Exception {

    public DuplicatedUsernameException() {
    }

    public DuplicatedUsernameException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
