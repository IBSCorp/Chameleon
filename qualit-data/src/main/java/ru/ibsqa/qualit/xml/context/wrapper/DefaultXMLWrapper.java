package ru.ibsqa.qualit.xml.context.wrapper;

import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.json.context.wrapper.IDataWrapper;
import ru.ibsqa.qualit.json.context.wrapper.PathException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

@Slf4j
public class DefaultXMLWrapper implements IDataWrapper {

    private final String CR = "\r";
    private final String LF = "\n";
    private final String CRLF = CR + LF;


    @Setter
    private String dataValue;

    /**
     * Получить фрагмент xml
     *
     * @return
     */
    @Override
    public String getDataValue(String locator) {
        return dataValue;
    }

    @Override
    public String getDataPretty(String locator) {
        return dataValue;
    }


    private Document context() {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = builderFactory.newDocumentBuilder();
            document = builder.parse(new ByteArrayInputStream(dataValue.getBytes()));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * Определение, существует ли поле
     *
     * @param locator
     * @return
     */
    @Override    //существует ли поле xml
    public boolean isExists(String locator) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile(locator).evaluate(context(), XPathConstants.NODESET);
            return nodeList.getLength() > 0;
        } catch (XPathExpressionException e) {
            return false;
        }
    }

    /**
     * Проверка наличия поля
     *
     * @param locator
     * @throws PathException
     */
    private void check(String locator) throws PathException {
        if (!isExists(locator)) {
            throw new PathException(ILocaleManager.message("xPathNotFoundErrorMessage", locator));
        }
    }

    /**
     * Установка значения существующего поля
     *
     * @param locator
     * @param value
     * @throws PathException
     */
    @Override
    public void set(String locator, Object value) throws PathException {
        // Проверим, что есть такой путь
        check(locator);
        Element element = context().getDocumentElement();
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile(locator).evaluate(element, XPathConstants.NODESET);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            if (nodeList != null) {
                log.info("Transform node elements by xpath '" + locator + "', set value '" + value + "'");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    log.info("Old value: " + nodeList.item(i).getTextContent());
                    nodeList.item(i).setTextContent(value.toString());
                    log.info("New value: " + nodeList.item(i).getTextContent());
                }
            }
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(element), new StreamResult(writer));
            this.dataValue = writer.getBuffer().toString();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    /**
     * Добавление нового поля TODO реализовать создание новой ноды XML
     *
     * @param locator
     * @param value
     * @throws PathException
     */
    @Override
    public void create(String locator, Object value) {

//        String jsonParentPath = "";
//        String key = "";
//
//        Pattern patternJsonKey = Pattern.compile("(.*)\\.(.*)");
//
//        Matcher matcher = patternJsonKey.matcher(locator + "");
//        if (matcher.find()) {
//            jsonParentPath = matcher.group(1);
//            key = matcher.group(2);
//        //    jsonValue = context().put(jsonParentPath, key, value).jsonString();
//        } else {
//            fail(ILocaleManager.message("matchJsonPathAndKeyError", locator));
//        }

    }

    /**
     * Удаление существующего поля TODO добавить удаление ноды
     *
     * @param locator
     * @throws PathException
     */
    @Override
    public void delete(String locator) throws PathException {
//        // Проверим, что есть такой путь
//        check(locator);
// //       jsonValue = context().delete(locator).jsonString();
    }

    //TODO добавить добавление ноды к xml
    @Override
    public void add(String locator, Object value) {
//        // Проверим, что есть такой путь
//        check(locator);
//   //     jsonValue = context().add(locator, value).jsonString();
    }

    /**
     * Считываем значение поля
     *
     * @param locator
     * @param type
     * @param <T>
     * @return
     * @throws PathException
     */
    @Override
    public <T> T read(String locator, Class<T> type) throws PathException {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile(locator).evaluate(context(), XPathConstants.NODESET);

            if (0 == nodeList.getLength()) {
                throw new PathException(ILocaleManager.message("xPathNotFoundErrorMessage", locator));
            }
            if (1 < nodeList.getLength()) {
                throw new PathException(ILocaleManager.message("xPathNotUniqueErrorMessage", locator));
            }
            Node object = nodeList.item(0);
            if (null == object) {
                return null;
            }
            return (T) object.getTextContent();


        } catch (XPathExpressionException e) {
            throw new PathException(ILocaleManager.message("xPathNotFoundErrorMessage", locator));
        }
    }


    /**
     * Валидируем xml по схеме
     *
     * @param schema
     */
    @Override
    public void validateSchema(String schema) {


    }


    @Override
    public long getDataLength(String locator) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            NodeList nodeList = (NodeList) xPath.compile(locator).evaluate(context(), XPathConstants.NODESET);
            return nodeList.getLength();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return -1;
    }


}
