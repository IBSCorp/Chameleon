package ru.ibsqa.chameleon.uia.driver;

public class NoWindowFoundException extends RuntimeException {

    NoWindowFoundException() {
        super();
    }

    NoWindowFoundException(String msg) {
        super(msg);
    }
}
