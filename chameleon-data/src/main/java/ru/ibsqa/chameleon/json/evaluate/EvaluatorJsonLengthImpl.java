package ru.ibsqa.chameleon.json.evaluate;


import ru.ibsqa.chameleon.context.IContextExplorer;
import ru.ibsqa.chameleon.elements.data.IFacadeDataExtractableField;
import ru.ibsqa.chameleon.evaluate.AbstractEvaluator;
import ru.ibsqa.chameleon.storage.IVariableScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Возвращает хэш md5
 * Пример использования:
 *      #arrayLength{Имя_массива_json}
 */
@Component
public class EvaluatorJsonLengthImpl extends AbstractEvaluator {

    @Autowired
    private IContextExplorer contextExplorer;

    @Override
    protected String getPlaceHolderName() {
        return "jsonLength";
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args) {
        String fieldName = args[0];
        IFacadeDataExtractableField field = contextExplorer.pickElement(fieldName, IFacadeDataExtractableField.class).getElement();
        return Long.toString(field.getDataLength());
    }

    @Override
    protected boolean isMultiArgs() {
        return false;
    }

}
