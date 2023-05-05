package ru.ibsqa.qualit.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "раньше или равно", messageName = "checkEARLIER_OR_EQUALS", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorEarlierOrEqualsImpl implements IComparator, IParseDate {
    @Override
    public boolean checkValue(String actual, String expected) {
        return !parseDate(expected).before(parseDate(actual));
    }
}
