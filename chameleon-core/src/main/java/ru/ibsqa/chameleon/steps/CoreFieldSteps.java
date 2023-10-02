package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.compare.ICompareManager;
import ru.ibsqa.chameleon.context.IContextExplorer;
import ru.ibsqa.chameleon.context.PickElementResult;
import ru.ibsqa.chameleon.elements.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.utils.waiting.ExploreResult;
import ru.ibsqa.chameleon.utils.waiting.Waiting;

import java.util.Optional;

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
        Waiting.on(field).apply(() ->
                field.setFieldValue(value)
        );
    }

    public String getFieldValue(String fieldName) {
        IFacadeReadable field = getField(fieldName, IFacadeReadable.class);
        return getFieldValue(field, fieldName);
    }

    @UIStep
    @TestStep("получено значение поля \"${fieldName}\"")
    private String getFieldValue(IFacadeReadable field, String fieldName) {
        String value = Waiting.on(field).get(field::getFieldValue);
        return Optional.ofNullable(value)
                .map(v -> v.trim().replaceAll("\u00A0", " "))
                .orElse(null);
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" очищено")
    public void clearField(String fieldName) {
        final IFacadeWritable field = getField(fieldName, IFacadeWritable.class);
        Waiting.on(field).apply(() -> {
            if (field instanceof IFacadeClearable) {
                ((IFacadeClearable) field).clearFieldValue();
            } else {
                field.setFieldValue("");
            }
        });
    }

    @UIStep
    @TestStep("значение поля \"${fieldName}\" ${operator} \"${expected}\"")
    public void checkFieldValue(String fieldName, String operator, String expected) {
        final IFacadeReadable field = getField(fieldName, IFacadeReadable.class);
        Waiting.on(field).explore(() -> {
                    final String actual = getFieldValue(field, fieldName);
                    return ExploreResult.create(
                            compareManager.checkValue(operator, actual, expected),
                            actual
                    );
                }
        ).ifNegative((actual) ->
                fail(compareManager.buildErrorMessage(operator, message("checkField"), actual, expected))
        );
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" присутствует")
    public void checkFieldExists(String fieldName) {
        IFacadeReadable field = getField(fieldName, IFacadeReadable.class);
        Waiting.on(field)
                .check(field::isFieldExists)
                .ifNegative(() ->
                        fail(message("fieldNotExistsAssertMessage", fieldName))
                );
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" отсутствует")
    public void checkFieldNotExists(String fieldName) {
        IFacadeReadable field = getField(fieldName, IFacadeReadable.class);
        Waiting.on(field).check(() -> {
            if (field instanceof IFacadeAbsent && ((IFacadeAbsent) field).isAbsent()) {
                return true;
            }
            return !field.isFieldExists();
        }).ifNegative(() ->
                fail(message("fieldExistsAssertMessage", fieldName))
        );
    }

    public <FACADE extends IFacade> FACADE getField(String fieldName, Class<FACADE> facade) {
        PickElementResult<FACADE, ?> pickElementResult = contextExplorer.pickElement(fieldName, facade);
        return pickElementResult.getElement();
    }

}
