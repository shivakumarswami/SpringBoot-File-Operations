package com.elixr.springbootfile.exceptionhandling;

public class InternalServerException extends  RuntimeException {

    public InternalServerException(String message) {

            super(message);
        }
    }

