package ru.ibsqa.chameleon.steps.aspect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StepType {
    private boolean uiStep;
    private boolean bddStep;
    private boolean setupStep;
    private boolean tearDownStep;
    public boolean isTestStep() {
        return !isBddStep() && !isSetupStep() && !isTearDownStep();
    }
}
