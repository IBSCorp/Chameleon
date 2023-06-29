package ru.ibsqa.chameleon.definitions.repository.selenium.adapters;

import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaElement;
import ru.ibsqa.chameleon.definitions.repository.selenium.elements.MetaCollection;

import javax.xml.bind.annotation.adapters.XmlAdapter;

// Добавлено для будущего применения, здесь можно выполнять дополнительные действия при маршализации/демаршализации
public class AdapterCollection extends XmlAdapter<MetaCollection,AbstractMetaElement> {
    @Override
    public AbstractMetaElement unmarshal(MetaCollection v) throws Exception {
        return v;
    }

    @Override
    public MetaCollection marshal(AbstractMetaElement v) throws Exception {
        return (MetaCollection)v;
    }
}
