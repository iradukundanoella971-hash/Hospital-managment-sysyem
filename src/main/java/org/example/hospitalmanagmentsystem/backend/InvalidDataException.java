package org.example.hospitalmanagmentsystem.backend;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}