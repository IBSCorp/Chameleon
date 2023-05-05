package ru.ibsqa.qualit.compare;

public interface ICompareManager {
    boolean checkValue(String operator, String actual, String expected);
    String buildErrorMessage(String operator, String prefix, String actual, String expected);
    String defaultOperator();
}
