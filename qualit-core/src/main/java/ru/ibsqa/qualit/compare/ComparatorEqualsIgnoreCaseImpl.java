package ru.ibsqa.qualit.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "равно игнорируя регистр", messageName = "checkEQUALS_IGNORE_CASE", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorEqualsIgnoreCaseImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return StringUtils.equalsIgnoreCase(actual, expected);
    }
}
