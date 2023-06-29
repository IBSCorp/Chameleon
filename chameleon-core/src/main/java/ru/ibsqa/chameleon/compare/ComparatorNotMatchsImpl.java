package ru.ibsqa.chameleon.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "не соответствует", messageName = "checkNOT_MATCH", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorNotMatchsImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return !StringUtils.defaultString(actual).matches(StringUtils.defaultString(expected));
    }
}
