package com.iocodes.app.util.handler;

import com.iocodes.app.api.exception.InvalidInputException;
import com.iocodes.app.api.exception.NotFoundException;
import com.iocodes.app.util.error.HttpErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(UNPROCESSABLE_CONTENT)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorResponse handleInvalidInputException (ServerHttpRequest request, InvalidInputException ex) {
        return createHttpErrorResponse(UNPROCESSABLE_CONTENT, request, ex);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorResponse handleNotFoundException (ServerHttpRequest request, NotFoundException ex) {
        return createHttpErrorResponse(NOT_FOUND, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody HttpErrorResponse handleNotFoundException (ServerHttpRequest request, NotFoundException ex) {
        return createHttpErrorResponse(NOT_FOUND, request, ex);
    }

    private HttpErrorResponse createHttpErrorResponse(HttpStatus httpStatus, ServerHttpRequest request, Exception ex) {
        return new HttpErrorResponse(ex.getMessage(), httpStatus.value(), httpStatus, request.getPath().value(), null);
    }
}
