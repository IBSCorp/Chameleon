package ru.ibsqa.qualit.tests;

import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.steps.WinRegistrySteps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
@Slf4j
public class WinRegTest {

    @Autowired
    private WinRegistrySteps winRegistrySteps;

    @Autowired
    private IEvaluateManager evaluateManager;

    @Test
    public void winRegTest() {
        log.info("-= Тест шагов для работы с реестром windows =-");

        final String key1="HKEY_CURRENT_USER\\SOFTWARE\\IBS";
        final String key2=key1+"\\qualit";
        final String value="123456789";
        winRegistrySteps.registryCreateKey(key1);
        winRegistrySteps.registryCreateKey(key2);

        final String value1Path=key2+"\\testValue1";
        winRegistrySteps.registrySetStringValue(value1Path, value);
        winRegistrySteps.registryGetValue("Переменная1", value1Path);
        assertEquals(value, (String) evaluateManager.evalVariable("#{Переменная1}"));

        final String value2Path=key2+"\\testValue2";
        winRegistrySteps.registrySetIntValue(value2Path, value);
        winRegistrySteps.registryGetValue("Переменная2", value2Path);
        assertEquals(value, (String) evaluateManager.evalVariable("#{Переменная1}"));

        final String value3Path=key2+"\\testValue3";
        winRegistrySteps.registrySetLongValue(value3Path, value);
        winRegistrySteps.registryGetValue("Переменная3", value3Path);
        assertEquals(value, (String) evaluateManager.evalVariable("#{Переменная3}"));

    }
}
