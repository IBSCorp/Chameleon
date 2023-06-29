package ru.ibsqa.chameleon.evaluate;


import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.storage.IVariableScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Возвращает хэш md5
 * Пример использования:
 *      #md5{passw0rd}
 */
@Component
@Evaluator(value = {
        "#md5{текст}"
}, priority = ConfigurationPriority.LOW)
@Slf4j
public class EvaluatorMd5Impl extends AbstractEvaluator {

    @Autowired
    private ILocaleManager localeManager;

    @Override
    protected String getPlaceHolderName() {
        return "md5";
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args) {
        String value = args[0];
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(value.getBytes());
            byte[] digest = md5.digest();
            return DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            fail(localeManager.getMessage("convert2md5ErrorMessage", value));
        }
        return null;
    }

    @Override
    protected boolean isMultiArgs() {
        return false;
    }

}
