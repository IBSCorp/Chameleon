package ru.ibsqa.chameleon.checker;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
@Lazy(value = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartCheckerManager {
    private List<IStartChecker> checkers;

    @Autowired
    private void collectCheckers(List<IStartChecker> checkers) {
        this.checkers = checkers;
        this.checkers.sort(Comparator.comparing(IStartChecker::getPriority));
    }

    @PostConstruct
    private void init() {
        if (Objects.nonNull(checkers)) {
            for (val checker : checkers) {
                checker.check();
            }
        }
    }
}
