package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.storage.IVariableScope;
import ru.ibsqa.qualit.storage.IVariableStorage;
import ru.ibsqa.qualit.utils.spring.SpringUtils;

public interface IEvaluateManager {
    default <T> T evalVariable(String param) {
        return evalVariable(SpringUtils.getBean(IVariableStorage.class).getDefaultScope(), param);
    }
    <T> T evalVariable(IVariableScope variableScope, String param);
}
