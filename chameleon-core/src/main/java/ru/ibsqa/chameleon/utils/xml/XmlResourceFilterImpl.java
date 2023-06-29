package ru.ibsqa.chameleon.utils.xml;

import org.springframework.stereotype.Component;

@Component
public class XmlResourceFilterImpl implements IXmlResourceFilter {
    @Override
    public boolean apply(XmlResource xmlResource) {
        return true;
    }
}
