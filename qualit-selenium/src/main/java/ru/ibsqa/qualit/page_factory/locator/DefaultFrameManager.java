package ru.ibsqa.qualit.page_factory.locator;

import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.utils.waiting.WaitingUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class DefaultFrameManager implements IFrameManager {

    @Autowired
    private IDriverManager driverManager;

    @Autowired
    private WaitingUtils waitingUtils;

    @Autowired
    private ISearchStrategy searchStrategy;

    private final String NO_FRAMES = "";
    private final String CURRENT_FRAME = ".";
    private final String PARENT_FRAME = "..";
    private ThreadLocal<String> currentPath = new ThreadLocal<>();
    private ThreadLocal<String> pageFrames = new ThreadLocal<>();
    private ThreadLocal<String> collectionFrames = new ThreadLocal<>();
    private ThreadLocal<String> elementFrames = new ThreadLocal<>();

    {
        currentPath.set(NO_FRAMES);
        pageFrames.set(NO_FRAMES);
        collectionFrames.set(NO_FRAMES);
        elementFrames.set(NO_FRAMES);
    }

    /**
     * Вызывать при переходе на новую страницу
     */
    @Override
    public void acceptPageFrames(final String[] frames) {
        pageFrames.set(arrayToPath(frames));
        collectionFrames.set(NO_FRAMES);
        elementFrames.set(NO_FRAMES);
        changeFrame(true);
    }

    /**
     * Вызывать при работе с коллекцией
     */
    @Override
    public void acceptCollectionFrames(final String[] frames) {
        collectionFrames.set(arrayToPath(frames));
        elementFrames.set(NO_FRAMES);
        changeFrame(false);
    }

    /**
     * Вызывать при работе с полем
     */
    @Override
    public void acceptElementFrames(final String[] frames) {
        elementFrames.set(arrayToPath(frames));
        changeFrame(false);
    }

    @Override
    public String getCurrentFramePath() {
        return currentPath.get();
    }

    @Override
    public void changeFramePath(String framePath) {
        framePath = Objects.isNull(framePath) ? NO_FRAMES : framePath;

        log.debug(String.format("changeFramePath(\"%s\")", framePath));

        WebDriverFacade driver = driverManager.getLastDriver();

        driver.switchToDefaultContent();
        if (driver.isFramesSupport()) {
            Stream.of(pathToArray(framePath)).forEach(frameLocator -> switchToFrame(driver, frameLocator));
        }
        //driver.switchTo().defaultContent();
        //Stream.of(pathToArray(framePath)).forEach(frameLocator -> switchToFrame(driver, frameLocator));

        currentPath.set(framePath);
    }

    private Stream<String> getAllFrames() {
        return Stream.of(
                Stream.of(pathToArray(pageFrames.get())),
                Stream.of(pathToArray(collectionFrames.get())),
                Stream.of(pathToArray(elementFrames.get()))
        ).reduce(Stream::concat)
                .orElseGet(Stream::empty);
    }

    private String computePath() {
        List<String> frames = getAllFrames().collect(Collectors.toList());
        if (frames.stream().anyMatch(CURRENT_FRAME::equals)) {
            return getCurrentFramePath();
        }
        int index = -1;
        while (-1 != (index = frames.indexOf(PARENT_FRAME))) {
            frames.remove(index);
            if (index > 0) {
                frames.remove(index - 1);
            }
        }
        return frames.stream().collect(Collectors.joining(">"));
    }

    /**
     * Поменять фрейм, если изменился путь
     */
    private void changeFrame(boolean forced) {
        String computedPath = computePath();
        if (forced || !computedPath.equals(getCurrentFramePath())) {
            changeFramePath(computedPath);
        }
    }

    /**
     * Переключиться на фрейм с ожиданием
     *
     * @param frameLocator
     */
    private void switchToFrame(WebDriverFacade driver, String frameLocator) {

        AtomicReference<WebDriverException> exception = new AtomicReference<>();
        if (!waitingUtils.waiting(driver.getDefaultWaitTimeOut() * 1000, () -> {
            try {
                if (frameLocator.matches("\\d+")) {
                    driver.switchTo().frame(Integer.parseInt(frameLocator));
                } else if (frameLocator.matches("^[a-zA-Z_$0-9]*$")) {
                    driver.switchTo().frame(frameLocator);
                } else {
                    driver.switchTo().frame(driver.findElement(searchStrategy.getLocator(frameLocator)));
                }
            } catch (WebDriverException e) {
                exception.set(e);
                return false;
            }
            return true;
        })) {
            throw exception.get();
        }
    }

}
