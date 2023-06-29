package ru.ibsqa.chameleon.json.context;

import ru.ibsqa.chameleon.context.IContextManager;
import ru.ibsqa.chameleon.context.IContextRegistrator;
import ru.ibsqa.chameleon.context.PickElementResult;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryManager;
import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.elements.data.AbstractDataField;
import ru.ibsqa.chameleon.elements.data.IFacadeDataMutableField;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.reporter.TestAttachment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Component
@Slf4j
public class ContextManagerJsonImpl implements IContextManagerJson, IContextRegistrator {

    @Override
    public String getContextName() {
        return localeManager.getMessage("jsonContextName");
    }

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IRepositoryManager repositoryManager;

    private ThreadLocal<IJsonLookObject> currentJson = new ThreadLocal<>();

    @Override
    public IJsonLookObject createJson(String lookName, String jsonValue) {
        IJsonLookObject jsonLookObject = new DefaultJsonLookObject();
        jsonLookObject.initialize(lookName, jsonValue);

        // Установим текущий контекст
        currentJson.set(jsonLookObject);
        return jsonLookObject;
    }

    @Override
    public IJsonLookObject openJson(String lookName, InputStream is, String charsetName) {
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(is, writer, charsetName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        return createJson(lookName, attachJsonInFile(writer.toString()));
    }

    @TestAttachment(value = "Текст файла", mimeType = "text/plain")
    private String attachJsonInFile(String text) {
        return text;
    }

    @Override
    public void saveJson(OutputStream os, String charsetName) {
        try {
            os.write(attachJsonInFile(getCurrentJson().toString()).getBytes(charsetName));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Override
    public IJsonLookObject getCurrentJson() {
        IJsonLookObject result = currentJson.get();
        assertNotNull(result, localeManager.getMessage("noCurrentJsonErrorMessage"));
        return result;
    }

    @Override
    public void clear() {
        currentJson.remove();
    }

    /**
     * Поиск элемента в тестовой среде
     * @param fullPathName полное название элемента, включая контекст и/или параметры (если они используются)
     * @param fieldType в этом параметре задается искомый фасад
     * @param <FACADE> искомый класс/интерфейс элемента = фасад
     * @return null, если элемент не найден
     */
    @Override
    public <FACADE extends IFacade> PickElementResult pickElement(String fullPathName, Class<FACADE> fieldType) {

        IJsonLookObject jsonLook = currentJson.get();

        // Если json не был установлен, то не ищем в нем
        if (null == jsonLook) {
            return null;
        }

        // Сформируем ответ
        PickElementResult<FACADE, IJsonLookObject> pickElementResult = PickElementResult
                .builder()
                .contextManager((IContextManager) this) // Ссылка на наш менеджер контекстов, который нашел
                .contextObject(jsonLook) // Ссылка на jsonlook, где найден элемент
                .build();

        IFacadeDataMutableField field = null;
        try {
            field = jsonLook.getFields().stream().filter(item -> ((AbstractDataField) item).getName().equals(fullPathName)).findFirst().get();
        } catch (NoSuchElementException e) {}

        pickElementResult.setElement((FACADE)field);  // Ссылка на найденный элемент

        if (null == pickElementResult.getElement()) {
            return null;
        }

        return pickElementResult;
    }
}
