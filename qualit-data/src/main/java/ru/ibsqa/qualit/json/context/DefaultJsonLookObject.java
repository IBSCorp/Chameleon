package ru.ibsqa.qualit.json.context;

import ru.ibsqa.qualit.definitions.repository.IRepositoryManager;
import ru.ibsqa.qualit.definitions.repository.data.AbstractDataMetaField;
import ru.ibsqa.qualit.definitions.repository.data.DataLook;
import ru.ibsqa.qualit.elements.IFacade;
import ru.ibsqa.qualit.elements.data.DefaultDataField;
import ru.ibsqa.qualit.elements.data.IFacadeDataMutableField;
import ru.ibsqa.qualit.json.context.wrapper.DefaultJsonWrapper;
import ru.ibsqa.qualit.json.context.wrapper.IDataWrapper;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import ru.ibsqa.qualit.reporter.TestAttachment;

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
