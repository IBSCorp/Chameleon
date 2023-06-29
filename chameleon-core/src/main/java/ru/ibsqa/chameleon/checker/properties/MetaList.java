package ru.ibsqa.chameleon.checker.properties;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@XmlRootElement(name = "List")
public class MetaList extends AbstractMetaVisibleProperty {

    @XmlElement(name = "Item", namespace = "http://chameleon.ibs-qa.ru/schema/properties")
    @Getter
    private List<MetaItem> items;

    @Override
    public PropertyType getType() {
        return PropertyType.List;
    }

    @Override
    public void validate(ValidateResult validateResult, String value) {
        super.validate(validateResult, value);
        if (validateResult.isValid()
                && StringUtils.isNoneEmpty(value)
                && Objects.nonNull(items)
                && items.stream().noneMatch(i -> i.getValue().equals(value))
        ) {
            validateResult.appendError(String.format("Значение [%s] входного параметра %s не соответствует допустимому списку [%s]",
                    value,
                    getLabel(),
                    items.stream().map(MetaItem::getLabel).collect(Collectors.joining(", "))
            ));
        }
    }
}
