package ru.ibsqa.qualit.reporter;

import org.jbehave.core.reporters.StoryReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JBehaveReporterManagerImpl implements IJBehaveReporterManager {

    private List<StoryReporter> reporters;

    @Autowired
    private void collectReporters(List<StoryReporter> reporters) {
        this.reporters = reporters;
    }

    @Override
    public StoryReporter[] getReporters() {
        return reporters.toArray(new StoryReporter[0]);
    }

}
