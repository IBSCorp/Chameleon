package ru.ibsqa.chameleon.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class FieldValueTable {

    @Getter @Setter
    private String field;

    @Getter @Setter
    private String value;
}
