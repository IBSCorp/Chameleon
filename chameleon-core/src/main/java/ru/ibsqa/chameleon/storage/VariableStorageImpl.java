package ru.ibsqa.chameleon.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class VariableStorageImpl implements IVariableStorage {

    private final ThreadLocal<IVariableScope> rootScope = new InheritableThreadLocal<>();

    private final ThreadLocal<IVariableScope> defaultScope = new InheritableThreadLocal<>();

    @Override
    public synchronized IVariableScope getRootScope() {
        IVariableScope result = rootScope.get();
        if (Objects.isNull(result)) {
            result = new VariableScopeImpl();
            rootScope.set(result);
        }
        return result;
    }

    @Override
    public synchronized void setDefaultScope(IVariableScope defaultScope) {
        this.defaultScope.set(defaultScope);
    }

    @Override
    public synchronized IVariableScope getDefaultScope() {
        return (null != defaultScope.get()) ? defaultScope.get() : getRootScope();
    }

    @Override
    public Map<String, Object> getVariables(IVariableScope variableScope){
        Map<String, Object> result = new HashMap<>();
        if (null != variableScope) {
            // Сначала значения из вышестоящих scope
            if (null != variableScope.getParent()) {
                result.putAll(getVariables(variableScope.getParent()));
            }
            // Значения из текущего scope замещают значения из вышестоящих
            result.putAll(variableScope.getVariables());
        }
        return result;
    }

    @Override
    public boolean hasVariable(IVariableScope variableScope, String name) {
        return getVariables(variableScope).containsKey(name);
    }

    @Override
    public Object getVariable(IVariableScope variableScope, String name) {
        return getVariables(variableScope).get(name);
    }

    @Override
    public void setVariable(IVariableScope variableScope, String name, Object value) {
        if (null != variableScope) {
            IVariableScope scope = variableScope;

            do {
                if (scope.hasVariable(name)) {
                    break;
                }
                scope = scope.getParent();
            } while (null != scope);

            if (null != scope) {
                scope.setVariable(name, value);
            } else {
                variableScope.setVariable(name, value);
            }
        }
    }

    @Override
    public void putVariables(IVariableScope variableScope, Map<String, Object> variables) {
        for ( Map.Entry<String, Object> variable : variables.entrySet() ) {
            setVariable(variable.getKey(), variable.getValue());
        }
    }

    @Override
    public void clearVariables(IVariableScope variableScope){
        if (null != variableScope) {
            variableScope.clearVariables();
        }
    }

}
