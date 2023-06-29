package ru.ibsqa.chameleon.evaluate;

import ru.ibsqa.chameleon.storage.IVariableScope;
import ru.ibsqa.chameleon.storage.IVariableStorage;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

public interface IEvaluateManager {
    default <T> T evalVariable(String param) {
        return evalVariable(SpringUtils.getBean(IVariableStorage.class).getDefaultScope(), param);
    }
    <T> T evalVariable(IVariableScope variableScope, String param);
}
