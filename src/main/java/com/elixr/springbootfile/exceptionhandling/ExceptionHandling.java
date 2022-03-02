package com.elixr.springbootfile.exceptionhandling;

import com.elixr.springbootfile.constants.Constants;
import com.elixr.springbootfile.response.ErrorResponse;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;


@RestControllerAdvice
@NoArgsConstructor
public class ExceptionHandling {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(buildErrorResponse(exception.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(Exception exception) {
        return new ResponseEntity<>(buildErrorResponse(exception.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(Exception exception) {
        return new ResponseEntity<>(buildErrorResponse(exception.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(Exception exception) {
        return new ResponseEntity<>(buildErrorResponse(exception.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(Exception exception) {
        return new ResponseEntity<>(buildErrorResponse(exception.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedTypeException(Exception exception) {
        return new ResponseEntity<>(buildErrorResponse(exception.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse buildErrorResponse(String message) {
        return ErrorResponse.builder().success(Constants.FAILURE).errorMsg(message).build();
    }
}
