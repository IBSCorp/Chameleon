package ru.ibsqa.qualit.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class VariableStorageImpl implements IVariableStorage {

    @Getter
    private IVariableScope rootScope = new VariableScopeImpl();

    @Setter
    private IVariableScope defaultScope;

    @Override
    public IVariableScope getDefaultScope() {
        return (null != defaultScope) ? defaultScope : rootScope;
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
