package ru.ibsqa.chameleon.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class VariableScopeImpl implements IVariableScope {

    @Getter @Setter(AccessLevel.PRIVATE)
    private IVariableScope parent;

    @Override
    public IVariableScope createChild() {
        VariableScopeImpl childScope = new VariableScopeImpl();
        childScope.setParent(this);
        return childScope;
    }

    @Getter
    private final Map<String, Object> variables = new HashMap<>();

    @Override
    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    @Override
    public Object getVariable(String name) {
        return variables.get(name);
    }

    @Override
    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    @Override
    public void putVariables( Map<String, Object> variables) {
        this.variables.putAll(variables);
    }

    @Override
    public void clearVariables(){
        variables.clear();
    }
}
