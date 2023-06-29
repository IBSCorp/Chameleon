package ru.ibsqa.chameleon.api.context;

import ru.ibsqa.chameleon.context.IContextManager;
import ru.ibsqa.chameleon.context.IContextRegistrator;
import ru.ibsqa.chameleon.context.PickElementResult;
import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.elements.api.IFacadeRequestField;
import ru.ibsqa.chameleon.elements.data.AbstractDataField;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
public class ContextManagerApiRequestImpl implements IContextManagerApiRequest, IContextRegistrator {

    @Override
    public String getContextName() {
        return localeManager.getMessage("requestContextName");
    }

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IContextManagerApiEndpoint contextManagerRestEndpoint;


    public void setCurrentRequest(IApiRequestObject currentRequest) {
        this.currentRequest.set(currentRequest);
    }

    private ThreadLocal<IApiRequestObject> currentRequest = new ThreadLocal<IApiRequestObject>();


    @Override
    public IApiRequestObject createRequest(String requestName, String template) {
        IApiRequestObject request = new DefaultApiRequestObject();
        request.initialize(contextManagerRestEndpoint.getCurrentEndpoint(), requestName, template);
        currentRequest.set(request);
        return request;
    }

    @Override
    public IApiRequestObject getCurrentRequest() {
        IApiRequestObject result = currentRequest.get();
        assertNotNull(result, localeManager.getMessage("noCurrentRequestErrorMessage"));
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

        IApiRequestObject request = currentRequest.get();

        // Если ответ еще не был создан, то не ищем в нем
        if (null == request) {
            return null;
        }

        // Сформируем ответ
        PickElementResult<FACADE, IApiRequestObject> pickElementResult = PickElementResult
                .builder()
                .contextManager((IContextManager) this) // Ссылка на наш менеджер контекстов, который нашел
                .contextObject(request) // Ссылка на запрос, где найден элемент
                .build();

        IFacadeRequestField field = null;
        try {
            field = request.getFields().stream().filter(
                    item -> ((AbstractDataField)item).getName().equals(fullPathName) && fieldType.isAssignableFrom(item.getClass())
            ).findFirst().get();
        } catch (NoSuchElementException e) {}

        pickElementResult.setElement((FACADE)field);  // Ссылка на найденный элемент

        if (null == pickElementResult.getElement()) {
            return null;
        }

        return pickElementResult;
    }
}
