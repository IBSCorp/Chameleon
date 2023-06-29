package ru.ibsqa.chameleon.definitions.repository.selenium.adapters;

import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaElement;
import ru.ibsqa.chameleon.definitions.repository.selenium.elements.MetaPage;

import javax.xml.bind.annotation.adapters.XmlAdapter;

// Добавлено для будущего применения, здесь можно выполнять дополнительные действия при маршализации/демаршализации
public class AdapterPage extends XmlAdapter<MetaPage,AbstractMetaElement> {
    @Override
    public AbstractMetaElement unmarshal(MetaPage v) throws Exception {
        return v;
    }

    @Override
    public MetaPage marshal(AbstractMetaElement v) throws Exception {
        return (MetaPage)v;
    }
}
