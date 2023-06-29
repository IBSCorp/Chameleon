package ru.ibsqa.chameleon.utils.error;

import java.util.Optional;

public class ErrorParser {
    public static String getErrorMessage(Throwable throwable) {
        return Optional.ofNullable(throwable.getMessage())
                .orElse(
                        Optional.ofNullable(throwable.getCause())
                                .map(Throwable::getMessage)
                                .orElse("")
                );
    }
}
