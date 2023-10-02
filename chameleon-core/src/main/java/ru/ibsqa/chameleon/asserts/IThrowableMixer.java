package ru.ibsqa.chameleon.asserts;

import java.util.List;
import java.util.Optional;

public interface IThrowableMixer {
    Optional<Throwable> mixThrowableFromList(List<Throwable> throwableList);
}
