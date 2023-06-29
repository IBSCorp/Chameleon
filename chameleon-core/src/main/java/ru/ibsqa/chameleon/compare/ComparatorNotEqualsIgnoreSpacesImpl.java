package ru.ibsqa.chameleon.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "не равно игнорируя пробелы", messageName = "checkNOT_EQUALS_IGNORE_SPACES", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorNotEqualsIgnoreSpacesImpl implements IComparator, IClearSpaces {
    @Override
    public boolean checkValue(String actual, String expected) {
        return !clearSpaces(actual).equals(clearSpaces(expected));
    }
}
