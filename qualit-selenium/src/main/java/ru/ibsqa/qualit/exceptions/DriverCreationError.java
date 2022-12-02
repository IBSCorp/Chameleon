package ru.ibsqa.qualit.exceptions;

public class DriverCreationError extends RuntimeException {
    public DriverCreationError(String msg) {
        super(msg);
    }
    public DriverCreationError(Throwable throwable) {
        super(throwable);
    }
    public DriverCreationError(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
