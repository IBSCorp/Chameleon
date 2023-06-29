package ru.ibsqa.chameleon.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "не содержит значение", messageName = "checkNOT_CONTAINS", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorNotContainsImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return !StringUtils.contains(actual, expected);
    }
}
