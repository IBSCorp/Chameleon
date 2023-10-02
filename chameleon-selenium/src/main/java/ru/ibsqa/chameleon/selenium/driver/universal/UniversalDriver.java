package ru.ibsqa.chameleon.selenium.driver.universal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.selenium.driver.WebDriverFacade;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/***
 * Универсальные драйвер, создается по умолчанию и использует универсальную фабрику.
 * Если создано несколько универсальных фабрик, то данный драйвер не работоспособен.
 */
@Component
public class UniversalDriver extends WebDriverFacade {

    @Autowired
    private IUniversalDriverFactory universalDriverFactory;


    @PostConstruct
    private void init() {
        setDriverFactory(universalDriverFactory);
    }

    @PreDestroy
    private void destroy() {
        this.quitAll();
    }
}
