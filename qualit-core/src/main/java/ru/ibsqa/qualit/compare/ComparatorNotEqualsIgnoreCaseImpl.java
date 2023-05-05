package ru.ibsqa.qualit.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "не равно игнорируя регистр", messageName = "checkNOT_EQUALS_IGNORE_CASE", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorNotEqualsIgnoreCaseImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return !StringUtils.equalsIgnoreCase(actual, expected);
    }
}
