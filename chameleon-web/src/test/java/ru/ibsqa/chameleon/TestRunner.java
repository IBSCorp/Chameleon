package ru.ibsqa.chameleon;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("chameleon-web")
@SelectPackages("ru.ibsqa.chameleon.tests")
public class TestRunner {
}
