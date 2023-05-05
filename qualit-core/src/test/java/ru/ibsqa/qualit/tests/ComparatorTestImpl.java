package ru.ibsqa.qualit.tests;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.compare.Comparator;
import ru.ibsqa.qualit.compare.IComparator;

@Comparator("тестовая операция")
@Component
public class ComparatorTestImpl implements IComparator {
    @Override
    public boolean checkValue(String actual, String expected) {
        return false;
    }
}
