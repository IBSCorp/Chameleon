package ru.ibsqa.chameleon.api.context;

import ru.ibsqa.chameleon.context.IContextManager;
import ru.ibsqa.chameleon.context.IContextRegistrator;
import ru.ibsqa.chameleon.context.PickElementResult;
import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.elements.api.IFacadeResponseField;
import ru.ibsqa.chameleon.elements.data.AbstractDataField;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
public class ContextManagerApiResponseImpl implements IContextManagerApiResponse, IContextRegistrator {

    @Override
    public String getContextName() {
        return localeManager.getMessage("responseContextName");
    }

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IContextManagerApiEndpoint contextManagerRestEndpoint;

    private ThreadLocal<IApiResponseObject> currentResponse = new ThreadLocal<IApiResponseObject>();

    @Override
    public IApiResponseObject receiveResponse(String responseName) {
        IApiResponseObject response = new DefaultApiResponseObject();
        response.initialize(contextManagerRestEndpoint.getCurrentEndpoint(), responseName);
        response.validateByMeta();
        currentResponse.set(response);
        return response;
    }

    @Override
    public IApiResponseObject getCurrentResponse() {
        IApiResponseObject result = currentResponse.get();
        assertNotNull(result, localeManager.getMessage("noCurrentResponseErrorMessage"));
        return result;
    }

    /**
     *
     * @param fullPathName
     * @param fieldType
     * @param <FACADE>
     * @return
     */
    @Override
    public <FACADE extends IFacade> PickElementResult pickElement(String fullPathName, Class<FACADE> fieldType) {

        IApiResponseObject response = currentResponse.get();

        // Если ответ еще не был получен, то не ищем в нем
        if (null == response) {
            return null;
        }

        // Сформируем ответ
        PickElementResult<FACADE, IApiRequestObject> pickElementResult = PickElementResult
                .builder()
                .contextManager((IContextManager) this) // Ссылка на наш менеджер контекстов, который нашел
                .contextObject(response) // Ссылка на ответ, где найден элемент
                .build();

        IFacadeResponseField field = null;
        try {
            field = response.getFields().stream().filter(
                    item -> ((AbstractDataField) item).getName().equals(fullPathName) && fieldType.isAssignableFrom(item.getClass())
            ).findFirst().get();
        } catch (NoSuchElementException e) {}

        pickElementResult.setElement((FACADE)field);  // Ссылка на найденный элемент

        if (null == pickElementResult.getElement()) {
            return null;
        }

        return pickElementResult;
    }
}
