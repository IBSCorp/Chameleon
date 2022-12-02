package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.converters.Variable;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonMutateStorySteps extends AbstractSteps {

    @Autowired
    private JsonMutateSteps jsonMutateSteps;

    @When("создано поле \"$fieldName\" в json со значением \"$value\"")
    public void createField(String fieldName, Variable value) {
        flow(()->
                jsonMutateSteps.createField(fieldName, value.getValue())
        );
    }

    @When("создан объект \"$fieldName\" в json")
    public void createObject(String fieldName) {
        flow(()->
                jsonMutateSteps.createObject(fieldName)
        );
    }

    @When("создан массив \"$fieldName\" в json")
    public void createArray(String fieldName) {
        flow(()->
                jsonMutateSteps.createArray(fieldName)
        );
    }

    @When("удалено поле \"$fieldName\" из json")
    public void deleteField(String fieldName) {
        flow(()->
                jsonMutateSteps.deleteField(fieldName)
        );
    }

    @When("в массив json \"$fieldName\" добавлено значение \"$value\"")
    public void addField(String fieldName, Variable value) {
        flow(()->
                jsonMutateSteps.addField(fieldName, value.getValue())
        );
    }

    @When("в массив json \"$fieldName\" добавлен объект")
    public void addObject(String fieldName) {
        flow(()->
                jsonMutateSteps.addObject(fieldName)
        );
    }

    @When("в массив json \"$fieldName\" добавлен массив")
    public void addArray(String fieldName) {
        flow(()->
                jsonMutateSteps.addArray(fieldName)
        );
    }

    @When("в качестве значения поля json \"$fieldName\" устанавливается объект")
    public void fillFieldAsObject(String fieldName){
        flow(()->
                jsonMutateSteps.fillFieldAsObject(fieldName)
        );
    }

    @When("в качестве значения поля json \"$fieldName\" устанавливается массив")
    public void fillFieldAsArray(String fieldName){
        flow(()->
                jsonMutateSteps.fillFieldAsArray(fieldName)
        );
    }

}