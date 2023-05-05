package ru.ibsqa.qualit.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "позже или равно", messageName = "checkLATER_OR_EQUALS", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorLaterOrEqualsImpl implements IComparator, IParseDate {
    @Override
    public boolean checkValue(String actual, String expected) {
        return !parseDate(expected).after(parseDate(actual));
    }
}
