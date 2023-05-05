package ru.ibsqa.qualit.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "раньше", messageName = "checkEARLIER", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorEarlierImpl implements IComparator, IParseDate {
    @Override
    public boolean checkValue(String actual, String expected) {
        return parseDate(expected).after(parseDate(actual));
    }
}
