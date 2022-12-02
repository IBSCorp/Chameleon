package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.context.IContextExplorer;
import ru.ibsqa.qualit.context.PickElementResult;
import ru.ibsqa.qualit.elements.IFacadeClearable;
import ru.ibsqa.qualit.elements.IFacadeReadable;
import ru.ibsqa.qualit.elements.IFacadeWritable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Component
@Primary
public class CoreFieldSteps extends AbstractSteps {

    @Autowired
    private IContextExplorer contextExplorer;

    @Autowired
    private CoreUtilSteps utilSteps;


    @UIStep
    @TestStep("поле \"${fieldName}\" заполняется значением \"${value}\"")
    public void fillField(String fieldName, String value) {
        PickElementResult<IFacadeWritable, ?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeWritable.class);
        IFacadeWritable field = pickElementResult.getElement();
        field.setFieldValue(value);
    }

    @UIStep
    @TestStep("получено значение поля \"${fieldName}\"")
    public String getFieldValue(String fieldName) {
        PickElementResult<IFacadeReadable, ?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeReadable.class);
        IFacadeReadable field = pickElementResult.getElement();
        return Optional.ofNullable(field.getFieldValue()).map(v -> v.trim().replaceAll("\u00A0", " ")).orElse(null);
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" очищено")
    public void clearField(String fieldName) {
        PickElementResult<IFacadeWritable, ?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeWritable.class);
        IFacadeWritable field = pickElementResult.getElement();
        if (field instanceof IFacadeClearable) {
            ((IFacadeClearable) field).clearFieldValue();
        } else {
            field.setFieldValue("");
        }
    }

    @UIStep
    @TestStep("значение поля \"${fieldName}\" ${operator} \"${expected}\"")
    public void checkFieldValue(String fieldName, CompareOperatorEnum operator, String expected) {
        String actual = getFieldValue(fieldName);
        if (!operator.checkValue(actual, expected)) {
            fail(operator.buildErrorMessage(message("checkField"), actual, expected));
        }
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" присутствует")
    public void checkFieldExists(String fieldName) {
        PickElementResult<IFacadeReadable, ?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeReadable.class);
        IFacadeReadable field = pickElementResult.getElement();
        assertTrue(field.isFieldExists(), message("fieldNotExistsAssertMessage", fieldName));
    }

    @UIStep
    @TestStep("поле \"${fieldName}\" отсутствует")
    public void checkFieldNotExists(String fieldName) {
        PickElementResult<IFacadeReadable, ?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeReadable.class);
        IFacadeReadable field = pickElementResult.getElement();
        assertFalse(field.isFieldExists(), message("fieldExistsAssertMessage", fieldName));
    }

}
