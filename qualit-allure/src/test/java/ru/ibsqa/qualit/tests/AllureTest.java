package ru.ibsqa.qualit.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.qualit.steps.CoreVariableSteps;
import ru.ibsqa.qualit.utils.spring.QualITSpringExtension;

@Slf4j
@ExtendWith(QualITSpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class AllureTest {
    @Autowired
    CoreVariableSteps coreVariableSteps;

    @Test
    public void allureTest() {
        log.info("allureTest");
        coreVariableSteps.createVariable("var1", "val1");
        coreVariableSteps.checkExpressionValue("#{var1}", "равно", "val1");
    }
}