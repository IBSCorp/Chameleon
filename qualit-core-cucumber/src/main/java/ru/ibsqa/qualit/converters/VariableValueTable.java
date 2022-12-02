package ru.ibsqa.qualit.converters;

import lombok.Getter;
import lombok.ToString;

@ToString
public class VariableValueTable {
    @Getter
    private String variable;

    @Getter
    private String value;
}
