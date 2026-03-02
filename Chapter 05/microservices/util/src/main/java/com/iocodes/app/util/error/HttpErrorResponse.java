package com.iocodes.app.util.error;

import org.springframework.http.HttpStatus;
import java.time.ZonedDateTime;

public record HttpErrorResponse(String message, int statusCode, HttpStatus status, String path, ZonedDateTime timestamp) {

    public HttpErrorResponse {
        timestamp = ZonedDateTime.now();
    }
}
