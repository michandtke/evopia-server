package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.NoRemoteUserFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEscalator {

    @ExceptionHandler(NoRemoteUserFoundException.class)
    public ResponseEntity handleException(NoRemoteUserFoundException e) {
        // log exception
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException e) {
        // log exception
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}
