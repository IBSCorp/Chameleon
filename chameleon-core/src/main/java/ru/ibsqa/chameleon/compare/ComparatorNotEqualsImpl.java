package ru.ibsqa.chameleon.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "не равно", messageName = "checkNOT_EQUALS", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorNotEqualsImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return !StringUtils.equals(actual, expected);
    }
}
