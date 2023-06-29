package ru.ibsqa.chameleon.tests;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.chameleon.asserts.SoftAssert;
import ru.ibsqa.chameleon.steps.TestStep;
import ru.ibsqa.chameleon.utils.spring.ChameleonSpringExtension;

import static org.hamcrest.Matchers.equalTo;

@Slf4j
@ExtendWith(ChameleonSpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class SoftAssertTest {

    @Test
    public void softAssertTest() {
        notEquals();

    }

    @TestStep
    public void notEquals() {
        int a = 1;
        int b = 2;
        SoftAssert.softAssertThat("значения не равны", a, Matchers.is(equalTo(b)));
    }
}
