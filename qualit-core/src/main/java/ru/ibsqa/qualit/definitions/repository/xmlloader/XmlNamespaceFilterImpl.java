package ru.ibsqa.qualit.definitions.repository.xmlloader;

import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;

@Component
public class XmlNamespaceFilterImpl implements IXmlNamespaceFilter {
    public XMLFilter getFilter(String namespace) {
        return new XMLFilterImpl() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                if (namespace.equals(uri)) {
                    super.startElement("", localName, qName, atts);
                } else {
                    super.startElement(uri, localName, qName, atts);
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                if (namespace.equals(uri)) {
                    super.endElement("", localName, qName);
                } else {
                    super.endElement(uri, localName, qName);
                }
            }

            @Override
            public void startPrefixMapping(String prefix, String url) throws SAXException {
                if (!namespace.equals(url)) {
                    super.startPrefixMapping(prefix, url);
                }
            }
        };
    }
}
