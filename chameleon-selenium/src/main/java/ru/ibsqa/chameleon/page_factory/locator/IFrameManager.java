package ru.ibsqa.chameleon.page_factory.locator;

import ru.ibsqa.chameleon.utils.spring.SpringUtils;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Управление переключением фреймов
 */
public interface IFrameManager {
    void acceptPageFrames(final String[] frames);

    void acceptCollectionFrames(final String[] frames);

    void acceptElementFrames(final String[] frames);

    String getCurrentFramePath();

    void changeFramePath(String framePath);

    default String arrayToPath(final String[] frames) {
        return Objects.isNull(frames) ? "" : Stream.of(frames).collect(Collectors.joining(">"));
    }

    default String[] pathToArray(final String path) {
        return Objects.isNull(path) || path.isEmpty() ? new String[0] : path.split("\\>");
    }

    /* Статические варианты для использования вне бинов */
    static void pageFrames(final String[] frames) {
        SpringUtils.getBean(IFrameManager.class).acceptPageFrames(frames);
    }

    static void collectionFrames(final String[] frames) {
        SpringUtils.getBean(IFrameManager.class).acceptCollectionFrames(frames);
    }

    static void elementFrames(final String[] frames) {
        SpringUtils.getBean(IFrameManager.class).acceptElementFrames(frames);
    }
}
