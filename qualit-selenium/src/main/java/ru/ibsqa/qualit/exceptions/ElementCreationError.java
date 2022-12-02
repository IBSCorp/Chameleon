package ru.ibsqa.qualit.exceptions;

public class ElementCreationError extends RuntimeException {
    public ElementCreationError(String msg) {
        super(msg);
    }
    public ElementCreationError(Throwable throwable) {
        super(throwable);
    }
    public ElementCreationError(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
