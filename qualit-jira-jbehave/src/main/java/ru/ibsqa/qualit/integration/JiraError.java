package ru.ibsqa.qualit.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class JiraError {

    @Getter @Setter
    private String message;

    public static JiraError parse(String responseString) {
        ObjectMapper mapper = new ObjectMapper();
        JiraError result = null;
        try {
            result = mapper.readValue(responseString, JiraError.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            result = new JiraError();
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
