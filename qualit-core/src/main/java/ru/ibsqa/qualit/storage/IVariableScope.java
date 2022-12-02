package ru.ibsqa.qualit.storage;

import java.util.Map;

public interface IVariableScope {
    IVariableScope getParent();
    IVariableScope createChild();

    Map<String, Object> getVariables();
    boolean hasVariable(String name);
    Object getVariable(String name);
    void setVariable(String name, Object value);
    void putVariables(Map<String, Object> variables);
    void clearVariables();

}
