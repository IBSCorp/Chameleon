package ru.ibsqa.chameleon.steps;

import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

public class JsonMutateStorySteps extends AbstractSteps {

    @Autowired
    private JsonMutateSteps jsonMutateSteps;

    @Тогда("^создано поле \"([^\"]*)\" в json со значением \"([^\"]*)\"$")
    public void createField(String fieldName, String value) {
        flow(()->
                jsonMutateSteps.createField(fieldName, value)
        );
    }

    @Тогда("^создан объект \"([^\"]*)\" в json$")
    public void createObject(String fieldName) {
        flow(()->
                jsonMutateSteps.createObject(fieldName)
        );
    }

    @Тогда("^создан массив \"([^\"]*)\" в json$")
    public void createArray(String fieldName) {
        flow(()->
                jsonMutateSteps.createArray(fieldName)
        );
    }

    @Тогда("^удалено поле \"([^\"]*)\" из json$")
    public void deleteField(String fieldName) {
        flow(()->
                jsonMutateSteps.deleteField(fieldName)
        );
    }

    @Тогда("^в массив json \"([^\"]*)\" добавлено значение \"([^\"]*)\"$")
    public void addField(String fieldName, String value) {
        flow(()->
                jsonMutateSteps.addField(fieldName, value)
        );
    }

    @Тогда("^в массив json \"([^\"]*)\" добавлен объект$")
    public void addObject(String fieldName) {
        flow(()->
                jsonMutateSteps.addObject(fieldName)
        );
    }

    @Тогда("^в массив json \"([^\"]*)\" добавлен массив$")
    public void addArray(String fieldName) {
        flow(()->
                jsonMutateSteps.addArray(fieldName)
        );
    }

    @Тогда("^в качестве значения поля json \"([^\"]*)\" устанавливается объект$")
    public void fillFieldAsObject(String fieldName){
        flow(()->
                jsonMutateSteps.fillFieldAsObject(fieldName)
        );
    }

    @Тогда("^в качестве значения поля json \"([^\"]*)\" устанавливается массив$")
    public void fillFieldAsArray(String fieldName){
        flow(()->
                jsonMutateSteps.fillFieldAsArray(fieldName)
        );
    }

}
