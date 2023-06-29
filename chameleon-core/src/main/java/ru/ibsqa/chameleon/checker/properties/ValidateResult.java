package ru.ibsqa.chameleon.checker.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidateResult {

    @Getter
    private boolean valid = true;

    private final StringBuilder messageBuilder = new StringBuilder();

    public void appendError(String message) {
        valid = false;
        if (messageBuilder.length()>0) {
            messageBuilder.append("\\n");
        }
        messageBuilder.append(message);
    }

    public String getMessage() {
        return messageBuilder.toString();
    }
}
