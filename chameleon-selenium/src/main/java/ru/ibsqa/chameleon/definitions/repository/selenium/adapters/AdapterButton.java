package ru.ibsqa.chameleon.definitions.repository.selenium.adapters;

import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaElement;
import ru.ibsqa.chameleon.definitions.repository.selenium.MetaButton;

import javax.xml.bind.annotation.adapters.XmlAdapter;

// Добавлено для будущего применения, здесь можно выполнять дополнительные действия при маршализации/демаршализации
public class AdapterButton extends XmlAdapter<MetaButton, AbstractMetaElement> {
    @Override
    public AbstractMetaElement unmarshal(MetaButton v) throws Exception {
        return v;
    }

    @Override
    public MetaButton marshal(AbstractMetaElement v) throws Exception {
        return (MetaButton)v;
    }
}
