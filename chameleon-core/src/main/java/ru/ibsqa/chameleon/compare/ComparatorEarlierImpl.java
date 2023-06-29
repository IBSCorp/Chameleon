package ru.ibsqa.chameleon.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "раньше", messageName = "checkEARLIER", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorEarlierImpl implements IComparator, IParseDate {
    @Override
    public boolean checkValue(String actual, String expected) {
        return parseDate(expected).after(parseDate(actual));
    }
}
