package ru.ibsqa.qualit.tests;

import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.storage.IVariableScope;
import ru.ibsqa.qualit.storage.IVariableStorage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class EvaluateTest {

    @Autowired
    private IVariableStorage variableStorage;

    @Autowired
    private IEvaluateManager evaluateManager;

    @Test
    public void evaluateTest() {
        log.info("-= Тест вычислений (новые) и переменных =-");

        final IVariableScope GLOBAL = variableStorage.getRootScope();
        final IVariableScope LOCAL = GLOBAL.createChild();
        variableStorage.setDefaultScope(LOCAL);

        String var = "1234.5";
        variableStorage.setVariable("Переменная1", var);
        assertEquals(var, evaluateManager.evalVariable("#{Переменная1}"));

        //Assert.assertEquals("1 234.50",(String)evaluateManager.evalVariable("#{decimal(#{Переменная1},{#,##0.00})}"));

        long current = System.currentTimeMillis();
        assertTrue(Long.parseLong(evaluateManager.evalVariable("#script{RESULT = System.currentTimeMillis()}"))>=current);

        long random = Long.parseLong(evaluateManager.evalVariable("#random{100;200}"));
        assertTrue(random>=100 && random<=200 );

        assertEquals("1 246,50", evaluateManager.evalVariable("#amount{#math{#{Переменная1} + 6*2}}"));
        assertEquals("1,234.50", evaluateManager.evalVariable("#amount{#{Переменная1};;'.';','}"));
        assertEquals("12_34.500", evaluateManager.evalVariable("#amount{#{Переменная1};'#,#0.000';'.';'_'}"));

        // TODO
        log.info(evaluateManager.evalVariable("#now{d llll yyyyг.;-45d}"));
        log.info(evaluateManager.evalVariable("#date{#now{01.MM.yy;+3M};dd.MM.yy;-1d}"));
        assertEquals("04.11.17", evaluateManager.evalVariable("#date{05.11.17;dd.MM.yy;-1d}"));

        variableStorage.setVariable("path", "classpath:/log4j.properties");
        log.info(evaluateManager.evalVariable("#file{#{path};UTF-8}"));

        log.info("--- Тест установки переменных различного уровня");
        variableStorage.setVariable(LOCAL, "Переменная2", "Значение2");
        variableStorage.setVariable(variableStorage.getRootScope(), "Переменная3", "Значение3");
        log.info("GLOBAL: " + variableStorage.getVariables(GLOBAL).toString());
        log.info("LOCAL: " + variableStorage.getVariables(LOCAL).toString());
        assertTrue(variableStorage.getVariables(GLOBAL).size()<variableStorage.getVariables(LOCAL).size());

        log.info("--- Тест тест присвоение значения переменной на более высоком уровне (теперь на разных уровнях разные значения)");
        variableStorage.setVariable(GLOBAL,"Переменная2", "Новое значение");
        log.info("GLOBAL: " + variableStorage.getVariables(GLOBAL).toString());
        log.info("LOCAL: " + variableStorage.getVariables(LOCAL).toString());
        assertTrue(variableStorage.getVariables(GLOBAL).size()>1);
        assertNotEquals(variableStorage.getVariable(GLOBAL,"Переменная2"),variableStorage.getVariable(LOCAL,"Переменная2"));

        log.info("--- Тест присвоения значение на более низком уровне");
        variableStorage.setVariable(LOCAL,"Переменная2", "Новое значение");
        log.info("GLOBAL: " + variableStorage.getVariables(GLOBAL).toString());
        log.info("LOCAL: " + variableStorage.getVariables(LOCAL).toString());
        assertTrue(variableStorage.getVariables(GLOBAL).size()>1);
        assertEquals(variableStorage.getVariable(GLOBAL,"Переменная2"),variableStorage.getVariable(LOCAL,"Переменная2"));

        log.info("--- Тест очистки локальных переменных");
        variableStorage.clearVariables(LOCAL);
        log.info("GLOBAL: " + variableStorage.getVariables(GLOBAL).toString());
        log.info("LOCAL: " + variableStorage.getVariables(LOCAL).toString());
        assertEquals(variableStorage.getVariables(GLOBAL).size(), variableStorage.getVariables(LOCAL).size());

        log.info("--- Тест очистки глобальных переменных");
        variableStorage.clearVariables(GLOBAL);
        log.info("GLOBAL: " + variableStorage.getVariables(GLOBAL).toString());
        log.info("LOCAL: " + variableStorage.getVariables(LOCAL).toString());
        assertEquals(0, variableStorage.getVariables(GLOBAL).size());

        log.info("--- Тест системных свойств");
        System.setProperty("qualit.my.property", "MyValue");
        assertEquals("MyValue", evaluateManager.evalVariable("#{qualit.my.property}"));

    }
}
