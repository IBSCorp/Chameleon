package ru.ibsqa.qualit.sap.sapEnum;

import ru.ibsqa.qualit.selenium.enums.KeyEnum;

public enum  SapKeyEnum {
    ENTER("00", KeyEnum.ENTER);

    private String value;

    private KeyEnum keyEnum;

    public KeyEnum getKeyEvent() {
        return keyEnum;
    }

    SapKeyEnum(String value, KeyEnum keyEnum) {
        this.value = value;
        this.keyEnum = keyEnum;
    }

    public String getValue() {
        return value;
    }
}
