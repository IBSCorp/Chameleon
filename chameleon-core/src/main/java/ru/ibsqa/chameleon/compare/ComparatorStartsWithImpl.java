package ru.ibsqa.chameleon.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "начинается с", messageName = "checkSTARTS_WITH", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorStartsWithImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return StringUtils.startsWith(actual, expected);
    }
}
