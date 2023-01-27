package ru.ibsqa.qualit;

import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.*;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;

@Suite
@SuiteDisplayName("qualit-winium-cucumber")
@IncludeEngines("cucumber")
@SelectDirectories("src/test/resources/features")
@CucumberContextConfiguration
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
@ExcludeTags("IGNORE")
public class TestRunner {
}