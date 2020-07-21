package com.example.linkconverter.exception;

public class ShortlinkNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public ShortlinkNotFoundException(String msg) {
        super(msg);
    }
}
