package ru.ibsqa.qualit.definitions.repository;

import ru.ibsqa.qualit.i18n.ILocaleManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Пустая имплементация менеджера репозитория
 */
//@Component
@Deprecated
public class RepositoryManagerStubImpl implements IRepositoryManager {

    @Autowired
    private ILocaleManager localeManager;

    @Override
    public <ELEMENT extends IRepositoryElement> List<ELEMENT> pickAllElements() {
        fail(localeManager.getMessage("repositoriesManagerErrorMessage"));
        return null;
    }

    @Override
    public <ELEMENT extends IRepositoryElement> ELEMENT pickElement(String s, Class<ELEMENT> aClass) {
        fail(localeManager.getMessage("repositoriesManagerErrorMessage"));
        return null;
    }

    @Override
    public void verify() {
    }
}
