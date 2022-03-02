package com.elixr.springbootfile.exceptionhandling;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {

        super(message);
    }
}

