package ru.ibsqa.chameleon.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "больше или равно", messageName = "checkGREATER_OR_EQUALS", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorGreaterOrEqualsImpl implements IComparator, IParseDouble {
    @Override
    public boolean checkValue(String actual, String expected) {
        return parseDouble(actual) >= parseDouble(expected);
    }
}
