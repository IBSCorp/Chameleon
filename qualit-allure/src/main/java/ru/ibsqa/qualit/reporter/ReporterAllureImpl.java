package ru.ibsqa.qualit.reporter;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureResultsWriteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.AllureConfiguration;
import ru.ibsqa.qualit.properties.AllureProperties;
import ru.ibsqa.qualit.utils.error.ErrorParser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

@Component
@Slf4j
public class ReporterAllureImpl extends AbstractReporter {

    private final static String ALLURE_ENVIRONMENT_FILE = "environment.xml";

    @Autowired
    private AllureProperties allureProperties;

    @Autowired
    private AllureConfiguration allureConfiguration;

    @Override
    public void createAttachment(String message, byte[] bytes, String mimeType, String extension, boolean isStepAttachment) {
        if (!allureConfiguration.isEnabled()) {
            return;
        }
        if (!isStepAttachment) {
            log.error("В отчетах allure поддерживаются только вложения к шагам, а не на уровне теста");
        }

        try {
            Allure.addByteAttachmentAsync(message, mimeType, extension, () -> bytes);
        } catch (AllureResultsWriteException e) {
            log.error("Не удалось приложить скриншот к отчету allure", e);
        }
    }


    /**
     * Метод копирует параметры запуска в файл allure-results\environment.xml для дальнейшего отображения их
     * в allure отчете. Метод также выводит ссылку на этот файл в консоль, чтобы была возможность посмотреть
     * параметры запуска во время работы теста.
     *
     * @param environment - текущие параметры запуска
     */
    @Override
    public void writeEnvironment(Environment environment) {
        if (!allureConfiguration.isEnabled()) {
            return;
        }

        if (Objects.isNull(environment) || environment.getParameters().isEmpty()) {
            return;
        }

        try {
            String allureResultsDirectory = allureProperties.getResultsDirectory();

            if (Objects.nonNull(allureResultsDirectory)) {
                String path = Paths.get(allureResultsDirectory, ALLURE_ENVIRONMENT_FILE).toString();
                File pathAsFile = new File(path);
                File folder = new File(pathAsFile.getParent());
                if (!folder.exists()) {
                    if (!folder.mkdirs()) {
                        throw new IOException(String.format("Не удалось создать каталог: %s", folder.getAbsolutePath()));
                    }
                }

                Marshaller marshaller = JAXBContext.newInstance(Environment.class).createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(environment, pathAsFile);

                log.info(String.format("Параметры запуска: %s", pathAsFile.getAbsolutePath()));
            }
        } catch (IOException | JAXBException e) {
            log.warn(String.format("Не удалось записать параметры запуска в environment.xml. Ошибка: %s",
                    ErrorParser.getErrorMessage(e)
            ));
        }
    }
}
