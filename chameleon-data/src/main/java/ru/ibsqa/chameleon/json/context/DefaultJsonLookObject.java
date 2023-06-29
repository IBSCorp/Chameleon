package ru.ibsqa.chameleon.json.context;

import ru.ibsqa.chameleon.definitions.repository.IRepositoryManager;
import ru.ibsqa.chameleon.definitions.repository.data.AbstractDataMetaField;
import ru.ibsqa.chameleon.definitions.repository.data.DataLook;
import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.elements.data.DefaultDataField;
import ru.ibsqa.chameleon.elements.data.IFacadeDataMutableField;
import ru.ibsqa.chameleon.json.context.wrapper.DefaultJsonWrapper;
import ru.ibsqa.chameleon.json.context.wrapper.IDataWrapper;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import ru.ibsqa.chameleon.reporter.TestAttachment;

import java.util.ArrayList;
import java.util.List;

public class DefaultJsonLookObject implements IJsonLookObject {

    private List<IFacadeDataMutableField> fields = new ArrayList<>();

    private IDataWrapper dataWrapper = new DefaultJsonWrapper();

    private IRepositoryManager repositoryManager = SpringUtils.getBean(IRepositoryManager.class);

    @Override
    public String toString() {
        return dataWrapper.getDataPretty();
    }

    @Override
    public List<IFacadeDataMutableField> getFields() {
        return fields;
    }

    @Override
    public <T extends IFacade> T getField(String fieldName) {
        return (T) getFields().stream().filter(item -> ((AbstractDataMetaField) item).getName().equals(fieldName)).findFirst().get();
    }

    @Override
    public void initialize(String lookName, String jsonValue) {

        setJsonValue(jsonValue);

        // Найти метаданные в репозитории
        DataLook metaData = repositoryManager.pickElement(lookName, DataLook.class);
        attachJsonLookMeta(metaData);

        // Проверить схему
        String schema = metaData.getSchema();
        if (null != schema && !schema.isEmpty()) {
            dataWrapper.validateSchema(schema);
        }

        // Заполнить список полей
        List<AbstractDataMetaField> metaFields = new ArrayList<>();
        metaFields.addAll(metaData.getFields());
        for (AbstractDataMetaField metaField : metaFields) {
            DefaultDataField fieldFacade = new DefaultDataField();
            fieldFacade.initialize(metaField, dataWrapper);
            fields.add(fieldFacade);
        }

    }

    @Override
    public void setJsonValue(String jsonValue) {
        dataWrapper.setDataValue(jsonValue);
    }

    @TestAttachment(value = "Метаданные облика файла", mimeType = "application/xml")
    private String attachJsonLookMeta(DataLook jsonLook) {
        return jsonLook.marshallXML();
    }

}
