package ru.ibsqa.chameleon.asserts;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ThrowableMixerImpl implements IThrowableMixer {
    @Override
    public Optional<Throwable> mixThrowableFromList(List<Throwable> throwableList) {
        if (Objects.isNull(throwableList) || throwableList.isEmpty()) {
            return Optional.empty();
        }
        Throwable throwable = new Throwable(
                throwableList
                        .stream()
                        .map(Throwable::getMessage)
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining("\n"))
        );
        throwableList.forEach(throwable::addSuppressed);
        return Optional.of(throwable);
    }
}
