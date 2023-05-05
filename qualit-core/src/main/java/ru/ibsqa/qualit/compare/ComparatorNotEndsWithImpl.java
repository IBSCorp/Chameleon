package ru.ibsqa.qualit.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "не оканчивается на", messageName = "checkNOT_ENDS_WITH", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorNotEndsWithImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return !StringUtils.endsWith(actual, expected);
    }
}
