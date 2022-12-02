package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.converters.Variable;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonFileStorySteps extends AbstractSteps {

    @Autowired
    private JsonFileSteps jsonFileSteps;

    @Given("загружен json с обликом \"$jsonLook\" из файла \"$fileName\" с кодировкой \"$charsetName\"")
    public void openJsonFromFile(String jsonLook, Variable fileName, Variable charsetName) {
        flow(()->
                jsonFileSteps.openJsonFromFile(jsonLook, fileName.getValue(), charsetName.getValue())
        );
    }

    @When("сохранен json в файл \"$fileName\" с кодировкой \"$charsetName\"")
    public void saveJsonToFile(Variable fileName, Variable charsetName) {
        flow(()->
                jsonFileSteps.saveJsonToFile(fileName.getValue(), charsetName.getValue())
        );
    }

    @When("закрыт файл json")
    public void closeJsonFile() {
        flow(()->
                jsonFileSteps.closeJsonFile()
        );
    }

    @When("в переменной \"$variable\" сохранен текст текущего файла json")
    public void fileToVariable(String variable) {
        flow(()->
                jsonFileSteps.fileToVariable(variable)
        );
    }

    @When("в текущий файл json присвоено значение выражения \"$variable\"")
    public void variableToFile(Variable variable) {
        flow(()->
                jsonFileSteps.variableToFile(variable.getValue())
        );
    }

    @Given("создан файл json с обликом \"$jsonLook\" на основе значения выражения \"$variable\"")
    public void createJsonFromVariable(String jsonLook, Variable variable) {
        flow(()->
                jsonFileSteps.createJsonFromVariable(jsonLook, variable.getValue())
        );
    }

}
