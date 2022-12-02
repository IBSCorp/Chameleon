package ru.ibsqa.qualit.context;

public interface IContextRegistrator {
    default IContextManager getContextManager() {
        return (IContextManager)this;
    }
}
