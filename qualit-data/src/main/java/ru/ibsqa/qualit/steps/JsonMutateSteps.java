package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.context.IContextExplorer;
import ru.ibsqa.qualit.context.PickElementResult;
import ru.ibsqa.qualit.elements.data.IFacadeDataMutableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Шаги для модификации структуры JSON
 */
@Component
public class JsonMutateSteps extends AbstractSteps {

    @Autowired
    private IContextExplorer contextExplorer;

    @TestStep("создано поле \"${fieldName}\" в json со значением \"${value}\"")
    public void createField(String fieldName, String value) {
        PickElementResult<IFacadeDataMutableField,?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeDataMutableField.class);
        IFacadeDataMutableField field = pickElementResult.getElement();
        field.createField(value);
    }

    @TestStep("создан объект \"${fieldName}\" в json")
    public void createObject(String fieldName) {
        PickElementResult<IFacadeDataMutableField,?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeDataMutableField.class);
        IFacadeDataMutableField field = pickElementResult.getElement();
        field.createObject();
    }

    @TestStep("создан массив \"${fieldName}\" в json")
    public void createArray(String fieldName) {
        PickElementResult<IFacadeDataMutableField,?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeDataMutableField.class);
        IFacadeDataMutableField field = pickElementResult.getElement();
        field.createArray();
    }

    @TestStep("удалено поле \"${fieldName}\" из json")
    public void deleteField(String fieldName) {
        PickElementResult<IFacadeDataMutableField,?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeDataMutableField.class);
        IFacadeDataMutableField field = pickElementResult.getElement();
        field.deleteField();
    }

    @TestStep("в массив json \"${fieldName}\" добавлено значение \"${value}\"")
    public void addField(String fieldName, String value) {
        PickElementResult<IFacadeDataMutableField,?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeDataMutableField.class);
        IFacadeDataMutableField field = pickElementResult.getElement();
        field.addField(value);
    }

    @TestStep("в массив json \"${fieldName}\" добавлен объект")
    public void addObject(String fieldName) {
        PickElementResult<IFacadeDataMutableField,?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeDataMutableField.class);
        IFacadeDataMutableField field = pickElementResult.getElement();
        field.addObject();
    }

    @TestStep("в массив json \"${fieldName}\" добавлен массив")
    public void addArray(String fieldName) {
        PickElementResult<IFacadeDataMutableField,?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeDataMutableField.class);
        IFacadeDataMutableField field = pickElementResult.getElement();
        field.addArray();
    }

    @TestStep("в качестве значения поля json \"${fieldName}\" устанавливается объект")
    public void fillFieldAsObject(String fieldName){
        PickElementResult<IFacadeDataMutableField,?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeDataMutableField.class);
        IFacadeDataMutableField field = pickElementResult.getElement();
        field.setObject();
    }

    @TestStep("в качестве значения поля json \"${fieldName}\" устанавливается массив")
    public void fillFieldAsArray(String fieldName){
        PickElementResult<IFacadeDataMutableField,?> pickElementResult = contextExplorer.pickElement(fieldName, IFacadeDataMutableField.class);
        IFacadeDataMutableField field = pickElementResult.getElement();
        field.setArray();
    }

}
