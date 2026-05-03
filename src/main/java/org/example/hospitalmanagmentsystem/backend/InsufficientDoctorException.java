package org.example.hospitalmanagmentsystem.backend;

public class InsufficientDoctorException extends RuntimeException {
    public InsufficientDoctorException(String message) {
        super(message);
    }
}