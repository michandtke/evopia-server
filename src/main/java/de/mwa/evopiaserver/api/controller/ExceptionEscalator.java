package de.mwa.evopiaserver.api.controller;

import de.mwa.evopiaserver.api.NoRemoteUserFoundException;
import de.mwa.evopiaserver.api.UnknownChannelException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class ExceptionEscalator {

    @ExceptionHandler(NoRemoteUserFoundException.class)
    public ResponseEntity handleException(NoRemoteUserFoundException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity handlePSQLException(PSQLException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    @ExceptionHandler(UnknownChannelException.class)
    public ResponseEntity handleUnknownChannelException(UnknownChannelException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity handleSQLException(SQLException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}
