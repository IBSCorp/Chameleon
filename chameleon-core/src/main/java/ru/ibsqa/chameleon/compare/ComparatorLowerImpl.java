package ru.ibsqa.chameleon.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "меньше", messageName = "checkLOWER", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorLowerImpl implements IComparator, IParseDouble {
    @Override
    public boolean checkValue(String actual, String expected) {
        return parseDouble(actual) < parseDouble(expected);
    }
}
