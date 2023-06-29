package ru.ibsqa.chameleon.elements;

public class InvokeFieldException extends RuntimeException {

    public InvokeFieldException() {
    }

    public InvokeFieldException(String message) {
        super(message);
    }

    public InvokeFieldException(Throwable cause) {
        super(cause);
    }

    public InvokeFieldException(String message, Throwable cause) {
        super(message, cause);
    }

}
