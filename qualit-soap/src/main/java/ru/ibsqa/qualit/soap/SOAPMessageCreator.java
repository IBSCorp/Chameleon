package ru.ibsqa.qualit.soap;

import com.predic8.wsdl.Binding;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wstool.creator.RequestCreator;
import com.predic8.wstool.creator.SOARequestCreator;
import com.predic8.xml.util.ResourceDownloadException;
import groovy.xml.MarkupBuilder;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component @Slf4j
public class SOAPMessageCreator implements ISOAPMessageCreator {

    private Definitions def;

    private StringWriter message = new StringWriter();


    public  void parseWsdl(String urlWsdl){
        WSDLParser parser = new WSDLParser();
        try {
            def = parser.parse(RestAssured.baseURI + urlWsdl);
        }catch (ResourceDownloadException e){
            try {
                def = parser.parse(urlWsdl);
            }catch (Exception ex){
                throw new AssertionError(String.format("Не удалось загрузить ресурсы по ссылке: [%s]. Получена ошибка [%s]", urlWsdl, ex.getMessage()));
            }
        }
    }


    public void createMessage(String operation){
        SOARequestCreator creator = new SOARequestCreator(def, new RequestCreator(), new MarkupBuilder(message));
        creator.createRequest(getLocalPortType(operation).getName(), operation, getBinding(operation).getName());
        log.info(message.toString());
    }


    public String modifyMessage(){
        String message = this.message.toString();
        Map<String, String> nameSpaceForDel = getTargetNameSpaceForDel();
        for( Map.Entry nameSpace : nameSpaceForDel.entrySet()){
            String prefix = nameSpace.getKey().toString();
            String value = nameSpace.getValue().toString();
            if (message.contains(prefix)){
                message =  message.replaceAll("<"+prefix+":", "<").replaceAll("</"+prefix+":", "</").replaceAll(" xmlns:"+prefix+"='" + value + "'\\s*/>", "/>");
                message =  message.replaceAll("<ns2:", "<").replaceAll("</ns2:", "</").replaceAll(" xmlns:ns2='" + value + "'\\s*/>", "/>");
            }
        }
        return message;
    }


    private Map<String, String> getTargetNameSpaceForDel(){
        return (Map<String, String>) def.getSchemas().get(0).getNamespaceContext();
    }

    private Binding getBinding(String operation){
        Optional<Binding> find = def.getBindings().stream()
                .filter(field -> (field.getOperations().stream().anyMatch(operation1 -> (operation1.getName().equals(operation)))))
                .findFirst();
        return find.orElseThrow(() -> new AssertionError(String.format("Не удалось создать запрос для операции [%s]", operation)));
    }

    private PortType getLocalPortType(String operation){
        Optional<PortType> find = def.getLocalPortTypes().stream()
                .filter(field -> (field.getOperations().stream().anyMatch(operation1 -> (operation1.getName().equals(operation)))))
                .findFirst();
        return find.orElseThrow(() -> new AssertionError(String.format("Не удалось создать запрос для операции [%s]", operation)));
    }


    public void removeEmptyNodes(org.w3c.dom.Node node) {
        NodeList list = node.getChildNodes();
        List<org.w3c.dom.Node> nodesToRecursivelyCall = new LinkedList();

        for (int i = 0; i < list.getLength(); i++) {
            nodesToRecursivelyCall.add(list.item(i));
        }

        for(org.w3c.dom.Node tempNode : nodesToRecursivelyCall) {
            removeEmptyNodes(tempNode);
        }

        boolean emptyElement = node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE
                && node.getChildNodes().getLength() == 0;
        boolean emptyText = node.getNodeType() == Node.TEXT_NODE
                && node.getNodeValue().trim().isEmpty();

        if (emptyElement || emptyText) {
            node.getParentNode().removeChild(node);
        }
    }


}
