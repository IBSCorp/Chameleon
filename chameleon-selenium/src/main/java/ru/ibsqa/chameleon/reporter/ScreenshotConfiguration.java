package ru.ibsqa.chameleon.reporter;

public enum  ScreenshotConfiguration {

    FOR_EACH_STEP("FOR_EACH_STEP"),
    DISABLED("DISABLED"),
    BEFORE_AND_AFTER_EACH_STEP("BEFORE_AND_AFTER_EACH_STEP"),
    FOR_FAILURES("FOR_FAILURES"),
    FOR_UI_FAILURES("FOR_UI_FAILURES");


    private String value;

    ScreenshotConfiguration(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
