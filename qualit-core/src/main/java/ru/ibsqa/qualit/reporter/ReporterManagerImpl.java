package ru.ibsqa.qualit.reporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.steps.visibility.IStepVisibilityManager;

import java.util.List;

@Component
public class ReporterManagerImpl implements IReporterManager {

    @Autowired
    private List<IReporter> reporters;

    @Autowired
    private IStepVisibilityManager stepVisibilityManager;

    @Override
    public void createAttachment(String message, byte[] bytes, String mimeType, String extension, boolean isStepAttachment) {
        if (stepVisibilityManager.isHidden()) {
            return;
        }
        reporters.forEach((r) -> r.createAttachment(message, bytes, mimeType, extension, isStepAttachment));
    }

    @Override
    public void writeEnvironment(Environment environment) {
        reporters.forEach((r) -> r.writeEnvironment(environment));
    }

}
