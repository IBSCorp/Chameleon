package ru.ibsqa.qualit.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "меньше", messageName = "checkLOWER", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorLowerImpl implements IComparator, IParseDouble {
    @Override
    public boolean checkValue(String actual, String expected) {
        return parseDouble(actual) < parseDouble(expected);
    }
}
