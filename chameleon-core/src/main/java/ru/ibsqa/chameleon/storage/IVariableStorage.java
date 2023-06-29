package ru.ibsqa.chameleon.storage;

import java.util.Map;

public interface IVariableStorage {

    IVariableScope getRootScope();

    IVariableScope getDefaultScope();
    void setDefaultScope(IVariableScope scope);

    default Map<String, Object> getVariables() {
        return getVariables(getDefaultScope());
    }

    Map<String, Object> getVariables(IVariableScope variableScope);

    default boolean hasVariable(String name) {
        return hasVariable(getDefaultScope(), name);
    }

    boolean hasVariable(IVariableScope variableScope, String name);

    default Object getVariable(String name) {
        return getVariable(getDefaultScope(), name);
    }

    Object getVariable(IVariableScope variableScope, String name);

    default void setVariable(String name, Object value) {
        setVariable(getDefaultScope(), name, value);
    }


    void setVariable(IVariableScope variableScope, String name, Object value);

    default void putVariables(Map<String, Object> variables) {
        putVariables(getDefaultScope(), variables);
    }

    void putVariables(IVariableScope variableScope, Map<String, Object> variables);

    default void clearVariables() {
        clearVariables( getDefaultScope());
    }

    void clearVariables(IVariableScope variableScope);

}
