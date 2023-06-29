package ru.ibsqa.chameleon.influx.providers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TagData {
    private String tagName;
    private String value;
}
