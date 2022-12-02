package ru.ibsqa.qualit.elements.data;

import ru.ibsqa.qualit.definitions.repository.data.AbstractDataMetaField;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractDataField {

    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PROTECTED)
    private AbstractDataMetaField metaField;

    public String getName() {
        String name = "";

        if (null != metaField && null != metaField.getName()) {
            name = metaField.getName();
        }

        return name;
    }

}
