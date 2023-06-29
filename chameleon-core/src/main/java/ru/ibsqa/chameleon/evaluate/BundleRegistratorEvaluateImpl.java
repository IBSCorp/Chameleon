package ru.ibsqa.chameleon.evaluate;

import ru.ibsqa.chameleon.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorEvaluateImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "evaluate";
    }
}
