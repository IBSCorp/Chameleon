package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.json.context.IContextManagerJson;
import ru.ibsqa.chameleon.storage.IVariableStorage;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Шаги для работы с файлами JSON
 */
@Component
@Slf4j
public class JsonFileSteps extends AbstractSteps {

    @Autowired
    private IVariableStorage variableStorage;

    @Autowired
    private IContextManagerJson contextManagerJson;

    @TestStep("загружен json с обликом \"${jsonLook}\" из файла \"${fileName}\" с кодировкой \"${charsetName}\"")
    public void openJsonFromFile(String jsonLook, String fileName, String charsetName ) {
        InputStream is = null;
        try {
            is = SpringUtils.openResourceOrFile(fileName);
            contextManagerJson.openJson(jsonLook, is, charsetName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(message("openFileErrorMessage", fileName));
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @TestStep("сохранен json в файл \"${fileName}\" с кодировкой \"${charsetName}\"")
    public void saveJsonToFile(String fileName, String charsetName) {
        FileOutputStream fos = null;
        File file;
        try {

            file = new File(fileName);
            fos = new FileOutputStream(file);

            if (!file.exists()) {
                file.createNewFile();
            }

            contextManagerJson.saveJson(fos, charsetName);

            fos.flush();

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(message("saveFileErrorMessage", fileName));
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @TestStep("в переменной \"${variable}\" сохранен текст текущего файла json")
    public void fileToVariable(String variable) {
        variableStorage.setVariable(variable, contextManagerJson.getCurrentJson().toString());
    }

    @TestStep("в текущий файл json присвоено значение \"${variable}\"")
    public void variableToFile(String variable) {
        contextManagerJson.getCurrentJson().setJsonValue(variable);
    }

    @TestStep("создан файл json с обликом \"${jsonLook}\" на основе значения \"${variable}\"")
    public void createJsonFromVariable(String jsonLook, String variable ) {
        contextManagerJson.createJson(jsonLook, variable);
    }

    @TestStep("закрыт файл json")
    public void closeJsonFile() {
        contextManagerJson.clear();
    }

}
