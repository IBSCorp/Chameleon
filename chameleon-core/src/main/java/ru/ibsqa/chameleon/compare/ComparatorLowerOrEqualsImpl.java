package ru.ibsqa.chameleon.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "меньше или равно", messageName = "checkLOWER_OR_EQUALS", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorLowerOrEqualsImpl implements IComparator, IParseDouble {
    @Override
    public boolean checkValue(String actual, String expected) {
        return parseDouble(actual) <= parseDouble(expected);
    }
}
