package ru.ibsqa.chameleon.selenium.driver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DriverSession {

    private static ObjectMapper mapper = new ObjectMapper();

    @Getter @Setter
    private String sessionId;

    @Getter
    @Setter
    private String url;

    public String toJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static DriverSession fromJson(String data) {
        try {
            return mapper.readValue(data, DriverSession.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
