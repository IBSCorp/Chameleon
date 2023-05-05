package ru.ibsqa.qualit.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "по длине меньше", messageName = "checkLENGTH_LOWER", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorLengthLowerImpl implements IComparator, IParseInt {
    @Override
    public boolean checkValue(String actual, String expected) {
        return StringUtils.defaultString(actual).length() < parseInt(expected);
    }
}
