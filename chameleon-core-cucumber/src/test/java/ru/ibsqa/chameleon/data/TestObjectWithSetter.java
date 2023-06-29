package ru.ibsqa.chameleon.data;

import lombok.Getter;

public class TestObjectWithSetter {
    @Getter
    private String field1;

    public void setField1(String field1) {
        this.field1 = field1 + "_setted";
    }
}
