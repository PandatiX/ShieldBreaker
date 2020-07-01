package com.shieldbreaker.cli.exceptions;

public class UnsupportedTypeException extends Exception {

    @Override
    public String getMessage() {
        return "Option type not supported.";
    }
}
