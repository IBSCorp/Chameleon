package ru.ibsqa.qualit.definitions.repository.db;

import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import java.util.List;
import java.util.stream.Collectors;

@ToString
public abstract class AbstractFieldList implements IRepositoryElement {

    @XmlAnyElement
    @XmlElementRefs({
            @XmlElementRef(name="FieldString", type=FieldString.class),
            @XmlElementRef(name="FieldInteger", type=FieldInteger.class),
            @XmlElementRef(name="FieldNumber", type=FieldNumber.class),
            @XmlElementRef(name="FieldBoolean", type=FieldBoolean.class)})
    private List<IRepositoryElement> fields;

    public List<AbstractField> getFields() {
        return fields
                .stream()
                .filter(item -> item instanceof AbstractField)
                .map(item -> (AbstractField)item)
                .collect(Collectors.toList());
    }

}
