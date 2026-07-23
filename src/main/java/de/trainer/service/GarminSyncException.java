package de.trainer.service;

public class GarminSyncException extends RuntimeException {
    public GarminSyncException(String message) {
        super(message);
    }

    public GarminSyncException(String message, Throwable cause) {
        super(message, cause);
    }
}
