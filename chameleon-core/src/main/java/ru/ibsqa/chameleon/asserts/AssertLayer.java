package ru.ibsqa.chameleon.asserts;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AssertLayer {

    @Getter
    private final AssertLayer parent;

    @Getter
    private final List<Throwable> errors;

    public boolean hasErrors() {
        return getErrors().size()>0;
    }

    public AssertLayer(AssertLayer parent) {
        this.parent = parent;
        this.errors = new ArrayList<>();
    }

}
