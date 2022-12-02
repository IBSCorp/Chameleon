package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.storage.IVariableScope;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Возвращает текст файла
 * Примеры использования:
 *      #file{C:\readme.txt;windows-1251}
 *      #file{classpath:/path/to/file/my.json}
 * через ; может быть указана кодировка файла, по умолчанию UTF-8
 */
@Component
@Evaluator({
        "#file{имя_файла;кодировка}",
        "#file{C:\\readme.txt;windows-1251}",
        "#file{classpath:/path/to/file/my.json}"
})
@Slf4j
public class EvaluatorFileImpl extends AbstractEvaluator {

    @Autowired
    private IEvaluateManager evaluateManager;

    @Override
    protected String getPlaceHolderName() {
        return "file";
    }

    @Override
    protected boolean isMultiArgs() {
        return true;
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args) {
        String fileName = this.extract("", args, 0);
        String charsetName = this.extract("UTF-8", args, 1);

        InputStream is = null;
        try {
            is = SpringUtils.openResourceOrFile(fileName);
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, charsetName);
            return writer.toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(localeManager.getMessage("openFileErrorMessage", fileName));
            return null;
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



}
