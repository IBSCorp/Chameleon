package ru.ibsqa.qualit.compare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.steps.aspect.IStepInformer;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Component
public class CompareManagerImpl implements ICompareManager {

    private List<IComparator> comparators;

    @Autowired
    private void collectComparators(List<IComparator> comparators) {
        this.comparators = comparators;
        this.comparators.sort(Comparator.comparing(IComparator::getPriority));
    }

    @Autowired
    private ILocaleManager localeManager;

    @Override
    public boolean checkValue(String operator, String actual, String expected) {
        return findComparator(operator).checkValue(actual, expected);
    }

    @Override
    public String buildErrorMessage(String operator, String prefix, String actual, String expected) {
        IComparator comparator = findComparator(operator);
        String message = Optional.ofNullable(comparator.getMessageName())
                .map(msg -> localeManager.getMessage(msg, actual, expected))
                .orElse(localeManager.getMessage("checkOPERATOR", operator, actual, expected));
        return (Objects.nonNull(prefix) ? (prefix + ": ") : "") + message;
    }

    @Override
    public String defaultOperator() {
        return "равно";
    }

    private IComparator findComparator(String operator) {
        assertNotNull(operator);
        IComparator comparator = comparators.stream()
                .filter(c -> c.getOperatorName().equalsIgnoreCase(operator.trim()))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(comparator)) {
            fail(localeManager.getMessage("operatorNotFound", operator));
        }

        return comparator;
    }
}
