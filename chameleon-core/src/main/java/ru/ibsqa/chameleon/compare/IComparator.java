package ru.ibsqa.chameleon.compare;

import org.apache.commons.lang3.StringUtils;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.i18n.ILocaleManager;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public interface IComparator {
    boolean checkValue(String actual, String expected);
    default String getMessageName() {
        Comparator annotation = this.getClass().getAnnotation(Comparator.class);
        if (Objects.nonNull(annotation) && StringUtils.isNotEmpty(annotation.messageName())) {
            return annotation.messageName();
        }
        return null;
    }
    default String getOperatorName() {
        Comparator annotation = this.getClass().getAnnotation(Comparator.class);
        if (Objects.isNull(annotation)) {
            fail(ILocaleManager.message("comparatorNotAnnotated", this.getClass().getCanonicalName()));
        }
        return annotation.value();
    }
    default ConfigurationPriority getPriority() {
        Comparator annotation = this.getClass().getAnnotation(Comparator.class);
        if (Objects.isNull(annotation)) {
            return ConfigurationPriority.NORMAL;
        }
        return annotation.priority();
    }
}
