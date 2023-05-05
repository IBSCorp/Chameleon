package ru.ibsqa.qualit.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "не начинается с", messageName = "checkNOT_STARTS_WITH", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorNotStartsWithImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return !StringUtils.startsWith(actual, expected);
    }
}
