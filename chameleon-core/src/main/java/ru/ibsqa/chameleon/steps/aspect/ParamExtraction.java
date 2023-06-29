package ru.ibsqa.chameleon.steps.aspect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ParamExtraction {
    private String key;
    private Object value;
}
