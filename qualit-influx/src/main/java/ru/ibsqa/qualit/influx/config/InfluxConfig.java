package ru.ibsqa.qualit.influx.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
public class InfluxConfig {

    @Setter
    @Getter
    private String url;

    @Setter
    @Getter
    private String username;

    @Setter
    @Getter
    private String password;

    @Getter
    @Setter
    private String database;

    @Getter
    @Setter
    private String measurement;

    @Getter
    @Setter
    private String retentionPolicy = "autogen";

    @Getter
    @Setter
    private boolean enabled = false;

    @Getter
    @Setter
    private boolean skipDefaultProviders = false;

}
