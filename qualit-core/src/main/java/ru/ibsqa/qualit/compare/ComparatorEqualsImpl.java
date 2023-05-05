package ru.ibsqa.qualit.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "равно", messageName = "checkEQUALS", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorEqualsImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return StringUtils.equals(actual, expected);
    }
}
