package ru.ibsqa.chameleon.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "больше", messageName = "checkGREATER", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorGreaterImpl implements IComparator, IParseDouble {
    @Override
    public boolean checkValue(String actual, String expected) {
        return parseDouble(actual) > parseDouble(expected);
    }
}
