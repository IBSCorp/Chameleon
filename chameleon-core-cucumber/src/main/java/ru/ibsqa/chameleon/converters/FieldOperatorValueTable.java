package ru.ibsqa.chameleon.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class FieldOperatorValueTable {

    @Getter
    @Setter
    private String field;

    @Getter
    @Setter
    private String operator;

    @Getter
    @Setter
    private String value;
}
