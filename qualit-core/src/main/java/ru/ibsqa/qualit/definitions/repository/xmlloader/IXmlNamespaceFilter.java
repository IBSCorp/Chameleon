package ru.ibsqa.qualit.definitions.repository.xmlloader;

import org.xml.sax.XMLFilter;

public interface IXmlNamespaceFilter {
    XMLFilter getFilter(String namespace);
}
