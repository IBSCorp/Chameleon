package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorEvaluateImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "evaluate";
    }
}
