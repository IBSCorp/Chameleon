package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.compare.ICompareManager;
import ru.ibsqa.chameleon.context.IContextExplorer;
import ru.ibsqa.chameleon.context.PickElementResult;
import ru.ibsqa.chameleon.elements.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@Component
@Primary
public class CoreFieldSteps extends AbstractSteps {

    @Autowired
    private IContextExplorer contextExplorer;

    @Autowired
    private ICompareManager compareManager;

    @UIStep
    @TestStep("поле \"${fieldName}\" заполняется значением \"${value}\"")
    public void fillField(String fieldName, String value) {
        IFacadeWritable field = getField(fieldName, IFacadeWritable.class);
        field.setFieldValue(value);
    }

    public String getFieldValue(String fieldName) {
        IFacadeReadable field = getField(fieldName, IFacadeReadable.class);
        return getFieldValue(field, fieldName);
    }

    @UIStep
    @TestStep("получено значение поля \"${fieldName}\"")
    private String getFieldValue(IFacadeReadable field, String fieldName) {
        return Optional.ofNullable(field.getFieldValue()).map(v -> v.trim().replaceAll("\u00A0", " ")).orElse(null);
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" очищено")
    public void clearField(String fieldName) {
        IFacadeWritable field = getField(fieldName, IFacadeWritable.class);
        if (field instanceof IFacadeClearable) {
            ((IFacadeClearable) field).clearFieldValue();
        } else {
            field.setFieldValue("");
        }
    }

    @UIStep
    @TestStep("значение поля \"${fieldName}\" ${operator} \"${expected}\"")
    public void checkFieldValue(String fieldName, String operator, String expected) {
        IFacadeReadable field = getField(fieldName, IFacadeReadable.class);
        AtomicReference<String> actual = new AtomicReference<>();
        boolean isChecked;
        if (field instanceof IFacadeWait) {
            isChecked = waiting(
                    Duration.ofSeconds(((IFacadeWait)field).getWaitTimeOut()),
                    () -> {
                        actual.set(getFieldValue(field, fieldName));
                        return compareManager.checkValue(operator, actual.get(), expected);
                    }
            );
        } else {
            isChecked = compareManager.checkValue(operator, getFieldValue(field, fieldName), expected);
        }
        if (!isChecked) {
            fail(compareManager.buildErrorMessage(operator, message("checkField"), actual.get(), expected));
        }
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" присутствует")
    public void checkFieldExists(String fieldName) {
        IFacadeReadable field = getField(fieldName, IFacadeReadable.class);
        boolean fieldExists;
        if (field instanceof IFacadeWait) {
            fieldExists = waiting(
                    Duration.ofSeconds(((IFacadeWait) field).getWaitTimeOut()),
                    field::isFieldExists
            );
        } else {
            fieldExists = field.isFieldExists();
        }
        if (!fieldExists) {
            fail(message("fieldNotExistsAssertMessage", fieldName));
        }
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" отсутствует")
    public void checkFieldNotExists(String fieldName) {
        IFacadeReadable field = getField(fieldName, IFacadeReadable.class);
        boolean fieldNotExists;
        if (field instanceof IFacadeWait) {
            fieldNotExists = waiting(
                    Duration.ofSeconds(((IFacadeWait) field).getWaitTimeOut()),
                    () -> {
                        if (field instanceof IFacadeAbsent && ((IFacadeAbsent) field).isAbsent()) {
                            return true;
                        }
                        return !field.isFieldExists();
                    }
            );
        } else {
            fieldNotExists = !field.isFieldExists();
        }
        if (!fieldNotExists) {
            fail(message("fieldExistsAssertMessage", fieldName));
        }
    }

    public <FACADE extends IFacade> FACADE getField(String fieldName, Class<FACADE> facade) {
        PickElementResult<FACADE, ?> pickElementResult = contextExplorer.pickElement(fieldName, facade);
        return pickElementResult.getElement();
    }

}
