package ru.ibsqa.qualit.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "по длине не равно", messageName = "checkLENGTH_NOT_EQUALS", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorLengthNotEqualsImpl implements IComparator, IParseInt {
    @Override
    public boolean checkValue(String actual, String expected) {
        return StringUtils.defaultString(actual).length() != parseInt(expected);
    }
}
