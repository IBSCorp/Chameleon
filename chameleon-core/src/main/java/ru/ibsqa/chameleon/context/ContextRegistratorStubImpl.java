package ru.ibsqa.chameleon.context;

import org.springframework.stereotype.Component;

@Component
public class ContextRegistratorStubImpl implements IContextRegistrator {
    @Override
    public IContextManager getContextManager() {
        return null;
    }
}
