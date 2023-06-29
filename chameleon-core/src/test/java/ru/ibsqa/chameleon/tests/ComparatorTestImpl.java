package ru.ibsqa.chameleon.tests;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.compare.Comparator;
import ru.ibsqa.chameleon.compare.IComparator;

@Comparator("тестовая операция")
@Component
public class ComparatorTestImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return false;
    }
}
