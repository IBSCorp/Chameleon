package ru.ibsqa.qualit.reporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReporterManagerImpl implements IReporterManager {

    private List<IReporter> reporters;

    @Autowired
    private void collectResolvers(List<IReporter> reporters) {
        this.reporters = reporters;
    }

    @Override
    public void createAttachment(String message, byte[] bytes, String mimeType, String extension, boolean isStepAttachment) {
        reporters.forEach((r) -> r.createAttachment(message, bytes, mimeType, extension, isStepAttachment));
    }

    @Override
    public void writeEnvironment(Environment environment) {
        reporters.forEach((r) -> r.writeEnvironment(environment));
    }

}
