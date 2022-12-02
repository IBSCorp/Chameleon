package ru.ibsqa.qualit.tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({H2Test.class})
public class AllTests {
}
