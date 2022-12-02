package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.storage.IVariableScope;

public interface IEvaluator {

    boolean matches(String param);
    <T> T evalVariable(IVariableScope variableScope, String param);

    default String prepareValue(String value) {
        if (null != value) {
            return value.replace("\\","\\\\").replace("$","\\$");
        }
        return "";
    }

}
