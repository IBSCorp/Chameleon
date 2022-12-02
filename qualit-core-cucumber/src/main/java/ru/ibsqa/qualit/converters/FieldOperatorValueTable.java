package ru.ibsqa.qualit.converters;

import ru.ibsqa.qualit.steps.CompareOperatorEnum;
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
    private CompareOperatorEnum operator;

    @Getter
    @Setter
    private String value;
}
