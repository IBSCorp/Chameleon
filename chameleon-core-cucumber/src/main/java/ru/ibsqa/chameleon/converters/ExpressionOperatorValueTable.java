package ru.ibsqa.chameleon.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ExpressionOperatorValueTable {

    @Getter
    @Setter
    private String expression;

    @Getter
    @Setter
    private String operator;

    @Getter
    @Setter
    private String value;

}
