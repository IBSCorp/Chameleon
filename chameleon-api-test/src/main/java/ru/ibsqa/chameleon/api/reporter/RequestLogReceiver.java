package ru.ibsqa.chameleon.api.reporter;

import ru.ibsqa.chameleon.reporter.TestAttachment;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RequestLogReceiver implements ILogReceiver {

    private final boolean attachContent = Boolean.parseBoolean(System.getProperty("api.attachContent", "true"));

    @Getter @Setter
    private boolean skipAttachBody = false;

    @Override
    public void receive(String text) {

        log.debug("---===begin of request===---");
        log.debug(text);
        log.debug("---===end of request===---");

        if (!attachContent) {
            return;
        }

        String[] parts = getParts(text);

        if (null != parts[0]) attachTitle(parts[0]);
        if (null != parts[1] && !isSkipAttachBody()) attachBody(parts[1]);
    }

    @TestAttachment(value = "Заголовок запроса", mimeType = "text/plain")
    public String attachTitle(String title) {
        return title;
    }

    @TestAttachment(value = "Тело запроса", mimeType = "text/plain")
    public String attachBody(String body) {
        return body;
    }
}
