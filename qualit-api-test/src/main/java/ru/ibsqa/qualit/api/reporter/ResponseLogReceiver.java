package ru.ibsqa.qualit.api.reporter;

import ru.ibsqa.qualit.reporter.TestAttachment;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResponseLogReceiver implements ILogReceiver {

    private final boolean attachContent = Boolean.parseBoolean(System.getProperty("api.attachContent", "true"));

    @Getter @Setter
    private boolean skipAttachBody = false;

    @Override
    public void receive(String text) {
        log.debug("---===begin of response===---");
        log.debug(text);
        log.debug("---===end of response===---");

        if (!attachContent) {
            return;
        }

        String[] parts = getParts(text);

        if (null != parts[0]) attachTitle(parts[0]);
        if (null != parts[1] && !isSkipAttachBody()) attachBody(parts[1]);
    }

    @TestAttachment(value = "Заголовок ответа", mimeType = "text/plain")
    public String attachTitle(String title) {
        return title;
    }

    @TestAttachment(value = "Тело ответа", mimeType = "text/plain")
    public String attachBody(String body) {
        return body;
    }

}
