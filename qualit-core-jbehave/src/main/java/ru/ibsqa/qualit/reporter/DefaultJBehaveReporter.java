package ru.ibsqa.qualit.reporter;

import lombok.extern.slf4j.Slf4j;
import org.jbehave.core.reporters.NullStoryReporter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultJBehaveReporter extends NullStoryReporter {
}