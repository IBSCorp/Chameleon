package ru.ibsqa.qualit.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.utils.spring.QualITSpringExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(QualITSpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class I18nTest {

    @Autowired
    private ILocaleManager localeManager;

    @Test
    public void l18nTest() {
        log.info("-= Тест локализации сообщений =-");

        String messageName = "evalErrorMessage";
        String messageText = "Error on evaluate expression [%s]";
        String expression = "выражение";
        assertEquals(
                String.format(messageText, expression),
                localeManager.getMessage("evalErrorMessage", Locale.ENGLISH, expression)
        );
    }
}
