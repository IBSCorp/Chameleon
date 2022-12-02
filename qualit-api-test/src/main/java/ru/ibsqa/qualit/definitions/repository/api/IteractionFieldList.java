package ru.ibsqa.qualit.definitions.repository.api;

import ru.ibsqa.qualit.definitions.repository.data.AbstractDataMetaFieldList;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.Unmarshaller;

@ToString(callSuper = true, exclude={"interaction"})
public abstract class IteractionFieldList extends AbstractDataMetaFieldList {

    @Getter
    private AbstractInteraction interaction;

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        this.interaction = (AbstractInteraction)parent;

    }

    public MetaEndpoint getEndpoint() {
        return (null == interaction) ? null : interaction.getEndpoint();
    }

}
