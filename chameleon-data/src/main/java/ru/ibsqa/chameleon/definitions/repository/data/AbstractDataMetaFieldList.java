package ru.ibsqa.chameleon.definitions.repository.data;

import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import java.util.List;
import java.util.stream.Collectors;

@ToString
public abstract class AbstractDataMetaFieldList implements IRepositoryElement {

    @XmlAnyElement
    @XmlElementRefs({
            @XmlElementRef(name="FieldString", type= FieldString.class),
            @XmlElementRef(name="FieldInteger", type= FieldInteger.class),
            @XmlElementRef(name="FieldNumber", type= FieldNumber.class),
            @XmlElementRef(name="FieldBoolean", type= FieldBoolean.class),
            @XmlElementRef(name="FieldObject", type= FieldObject.class),
            @XmlElementRef(name="FieldArray", type= FieldArray.class)})
    private List<IRepositoryElement> fields;

    public List<AbstractDataMetaField> getFields() {
        return fields
                .stream()
                .filter(item -> item instanceof AbstractDataMetaField)
                .map(item -> (AbstractDataMetaField)item)
                .collect(Collectors.toList());
    }

}
