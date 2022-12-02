package ru.ibsqa.qualit.api.reporter;

import io.restassured.config.LogConfig;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class ApiLoggerImpl implements IApiLogger {

    private static final String CODE_PAGE = "UTF-8";

    @Getter
    private LogConfig logConfig;

    private Streamer streamer;

    private PrintStream captor;

    @Override
    public void flush(ILogReceiver logReceiver) {
        String text = streamer.getOutput();
        logReceiver.receive(text);
    }

    @PostConstruct
    private void initialization() {
        streamer = new Streamer();
        try {
            captor = new PrintStream(streamer, true, CODE_PAGE);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        logConfig = new LogConfig(captor, true);
    }

    public static class Streamer extends ByteArrayOutputStream {

        public String getOutput() {
            String result = null;
            try {
                result = toString(CODE_PAGE);
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
            this.reset();
            return result;
        }
    }

}
