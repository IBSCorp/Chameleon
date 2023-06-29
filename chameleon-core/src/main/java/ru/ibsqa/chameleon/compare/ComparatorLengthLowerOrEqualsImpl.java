package ru.ibsqa.chameleon.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

@Comparator(value = "по длине не больше", messageName = "checkLENGTH_LOWER_OR_EQUALS", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorLengthLowerOrEqualsImpl implements IComparator, IParseInt {
    @Override
    public boolean checkValue(String actual, String expected) {
        return StringUtils.defaultString(actual).length() <= parseInt(expected);
    }
}
