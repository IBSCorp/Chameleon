package ru.ibsqa.chameleon.configuration.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@Component
public class StepJBehavePackageCollectorImpl implements IJBehaveStepPackageCollector {

    private List<IJBehaveStepsRegistrator> jBehaveStepsRegistrators;

    @Autowired
    private void collectJBehaveStepPackages(List<IJBehaveStepsRegistrator> jBehaveStepsRegistrators){
        this.jBehaveStepsRegistrators = jBehaveStepsRegistrators;
    }

    @Override
    public String[] getJBehaveStepRegistrators() {
        List<String> packages = new ArrayList<>();
        for (IJBehaveStepsRegistrator stepRegistrator: jBehaveStepsRegistrators){
            packages.add(stepRegistrator.getStepsPackage());
        }
        return packages.stream().distinct().toArray(String[]::new);
    }
}
