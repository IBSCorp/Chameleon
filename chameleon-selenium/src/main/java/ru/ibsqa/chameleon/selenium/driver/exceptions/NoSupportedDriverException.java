package ru.ibsqa.chameleon.selenium.driver.exceptions;

public class NoSupportedDriverException extends RuntimeException {
    public NoSupportedDriverException(String message) {
        super(message);
    }
}