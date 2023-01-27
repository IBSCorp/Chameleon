package ru.ibsqa.qualit;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("qualit-core")
@SelectPackages("ru.ibsqa.qualit.tests")
public class TestRunner {
}
