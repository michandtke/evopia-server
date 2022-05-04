package de.mwa.evopiaserver.api;

public class NoRemoteUserFoundException extends RuntimeException {
    public NoRemoteUserFoundException(String message) {
        super(message);
    }
}