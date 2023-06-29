package ru.ibsqa.chameleon.context;

public interface IContextRegistrator {
    default IContextManager getContextManager() {
        return (IContextManager)this;
    }
}
