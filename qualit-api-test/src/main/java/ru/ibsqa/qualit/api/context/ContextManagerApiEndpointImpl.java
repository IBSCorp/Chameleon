package ru.ibsqa.qualit.api.context;

import ru.ibsqa.qualit.i18n.ILocaleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
public class ContextManagerApiEndpointImpl implements IContextManagerApiEndpoint {

    @Override
    public String getContextName() {
        return localeManager.getMessage("endpointContextName");
    }

    @Autowired
    private ILocaleManager localeManager;

    private ThreadLocal<IApiEndpointObject> currentEndpoint = new ThreadLocal<IApiEndpointObject>();

    @Override
    public void setCurrentEndpoint(IApiEndpointObject endpoint) {
        currentEndpoint.set(endpoint);
    }

    @Override
    public IApiEndpointObject getCurrentEndpoint() {
        IApiEndpointObject result = currentEndpoint.get();
        assertNotNull(result, localeManager.getMessage("noCurrentEndpointErrorMessage"));
        return result;
    }

}
