package ru.ibsqa.qualit.context;

import org.springframework.stereotype.Component;

@Component
public class ContextRegistratorStubImpl implements IContextRegistrator {
    @Override
    public IContextManager getContextManager() {
        return null;
    }
}
