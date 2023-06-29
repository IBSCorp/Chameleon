package ru.ibsqa.chameleon.definitions.repository.xmlloader;

import org.xml.sax.XMLFilter;

public interface IXmlNamespaceFilter {
    XMLFilter getFilter(String namespace);
}
