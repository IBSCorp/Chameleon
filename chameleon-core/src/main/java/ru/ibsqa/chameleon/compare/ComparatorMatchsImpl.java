package ru.ibsqa.chameleon.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "соответствует", messageName = "checkMATCHS", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorMatchsImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return StringUtils.defaultString(actual).matches(StringUtils.defaultString(expected));
    }
}
