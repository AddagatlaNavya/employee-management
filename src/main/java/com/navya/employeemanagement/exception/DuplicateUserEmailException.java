package com.navya.employeemanagement.exception;

public class DuplicateUserEmailException extends RuntimeException {
    public DuplicateUserEmailException(String message) {
        super(message);
    }
}
