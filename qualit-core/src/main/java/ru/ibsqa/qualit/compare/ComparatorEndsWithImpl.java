package ru.ibsqa.qualit.compare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

@Comparator(value = "оканчивается на", messageName = "checkENDS_WITH", priority = ConfigurationPriority.LOW)
@Component
public class ComparatorEndsWithImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return StringUtils.endsWith(actual, expected);
    }
}
