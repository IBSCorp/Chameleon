package ru.ibsqa.chameleon.steps;


import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

public class JsonFileStorySteps extends AbstractSteps {

    @Autowired
    private JsonFileSteps jsonFileSteps;

    @Дано("^загружен json с обликом \"([^\"]*)\" из файла \"([^\"]*)\" с кодировкой \"([^\"]*)\"$")
    public void openJsonFromFile(
            String jsonLook,
            String fileName,
            String charsetName) {
        flow(()->
                jsonFileSteps.openJsonFromFile(jsonLook, fileName, charsetName)
        );
    }

    @Тогда("^сохранен json в файл \"([^\"]*)\" с кодировкой \"([^\"]*)\"$")
    public void saveJsonToFile(
            String fileName,
            String charsetName) {
        flow(()->
                jsonFileSteps.saveJsonToFile(fileName, charsetName)
        );
    }

    @Тогда("^закрыт файл json$")
    public void closeJsonFile() {
        flow(()->
                jsonFileSteps.closeJsonFile()
        );
    }

    @Тогда("^в переменной \"([^\"]*)\" сохранен текст текущего файла json$")
    public void fileToVariable(String variable) {
        flow(()->
                jsonFileSteps.fileToVariable(variable)
        );
    }

    @Тогда("^в текущий файл json присвоено значение выражения \"([^\"]*)\"$")
    public void variableToFile(String variable) {
        flow(()->
                jsonFileSteps.variableToFile(variable)
        );
    }

    @Дано("^создан файл json с обликом \"([^\"]*)\" на основе значения выражения \"([^\"]*)\"$")
    public void createJsonFromVariable(
            String jsonLook,
            String variable ) {
        flow(()->
                jsonFileSteps.createJsonFromVariable(jsonLook, variable)
        );
    }

}