package com.fujitsu.us.oovn.exception;

public class InvalidPortNumberException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public InvalidPortNumberException(final String msg) {
        super(msg);
    }
}
