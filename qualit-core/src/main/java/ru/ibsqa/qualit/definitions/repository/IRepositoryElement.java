package ru.ibsqa.qualit.definitions.repository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

public interface IRepositoryElement {

    default String marshallXML() {
        StringWriter stringWriter = new StringWriter();
        String xml = null;

        // Сформируем XML
        try {
            JAXBContext context = JAXBContext.newInstance(this.getClass());
            Marshaller marshaller = context.createMarshaller();
            //marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            marshaller.marshal(this, stringWriter);

            xml = stringWriter.toString();

        } catch (JAXBException e) {
            Logger log = LoggerFactory.getLogger(IRepositoryElement.class);
            log.error(e.getMessage(), e);
            return null;
        }

        // Отформатируем красиво
        try (Reader reader = new StringReader(xml)) {

            Document document = null;

            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource(reader);
                document = db.parse(is);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                Logger log = LoggerFactory.getLogger(IRepositoryElement.class);
                log.error(e.getMessage(), e);
                return null;
            }

            DOMImplementationRegistry registry = null;
            DOMImplementationLS impls = null;
            LSOutput domOutput = null;
            LSSerializer domWriter = null;
            try {
                registry =  DOMImplementationRegistry.newInstance();
                impls =  (DOMImplementationLS)registry.getDOMImplementation("LS");

                //Prepare the output
                domOutput = impls.createLSOutput();
                //domOutput.setEncoding(java.nio.charset.Charset.defaultCharset().name());
                stringWriter = new StringWriter();
                domOutput.setCharacterStream(stringWriter);
                domOutput.setEncoding("UTF-8");

                //Prepare the serializer
                domWriter = impls.createLSSerializer();
                DOMConfiguration domConfig = domWriter.getDomConfig();
                domConfig.setParameter("format-pretty-print", true);
                domConfig.setParameter("element-content-whitespace", true);
                domWriter.setNewLine("\r\n");

                //And finally, write
                domWriter.write(document, domOutput);
                xml = domOutput.getCharacterStream().toString();
                //DOMStringList dsl=domConfig.getParameterNames();
            } catch (ClassCastException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                Logger log = LoggerFactory.getLogger(IRepositoryElement.class);
                log.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            Logger log = LoggerFactory.getLogger(IRepositoryElement.class);
            log.error(e.getMessage(), e);
            return null;
        }

        return xml;
    }
}
