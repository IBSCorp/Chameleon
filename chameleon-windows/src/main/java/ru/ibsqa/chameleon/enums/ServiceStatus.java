package ru.ibsqa.chameleon.enums;

import lombok.Getter;

public enum ServiceStatus {
    UNKNOWS(-1),
    STOPPED(1),
    START_PENDING(2),
    STOP_PENDING(3),
    RUNNING(4)
    ;

    @Getter
    private int code;

    ServiceStatus(int code) {
        this.code = code;
    }

}
