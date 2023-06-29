package ru.ibsqa.chameleon.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "позже", messageName = "checkLATER", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorLaterImpl implements IComparator, IParseDate {
    @Override
    public boolean checkValue(String actual, String expected) {
        return parseDate(expected).before(parseDate(actual));
    }
}
