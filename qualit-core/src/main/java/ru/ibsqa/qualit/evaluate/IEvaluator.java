package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.compare.Comparator;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.storage.IVariableScope;

import java.util.Objects;

public interface IEvaluator {

    boolean matches(String param);
    <T> T evalVariable(IVariableScope variableScope, String param);

    default String prepareValue(String value) {
        if (null != value) {
            return value.replace("\\","\\\\").replace("$","\\$");
        }
        return "";
    }

    default ConfigurationPriority getPriority() {
        Evaluator annotation = this.getClass().getAnnotation(Evaluator.class);
        if (Objects.isNull(annotation)) {
            return ConfigurationPriority.NORMAL;
        }
        return annotation.priority();
    }

}
