package ru.ibsqa.chameleon.reporter;

import org.jbehave.core.reporters.StoryReporter;

public interface IJBehaveReporterManager {
    StoryReporter[] getReporters();
}
