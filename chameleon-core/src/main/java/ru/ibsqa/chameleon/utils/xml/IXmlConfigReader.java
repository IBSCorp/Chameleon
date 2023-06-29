package ru.ibsqa.chameleon.utils.xml;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

import java.util.List;

public interface IXmlConfigReader {
    Document getDocument(Resource resource);
    List<Resource> getResourcesByNamespace(XmlConfigScope scope, String namespace);
    List<Resource> getResourcesByRootTag(XmlConfigScope scope, String root);
}
