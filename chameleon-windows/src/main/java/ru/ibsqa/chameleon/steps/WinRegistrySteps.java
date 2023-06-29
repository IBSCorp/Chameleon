package ru.ibsqa.chameleon.steps;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinReg;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sun.jna.platform.win32.WinReg.*;
import static org.junit.jupiter.api.Assertions.*;

@Component
public class WinRegistrySteps extends AbstractSteps {

    //TODO удаление разделов, значений, проверка наличия значений, сохранение других типов значений

    @TestStep("в переменную \"${variable}\" сохранено значение \"${path}\" из реестра windows")
    public void registryGetValue(String variable, String path) {
        try {
            WinRegPath winRegPath = getHKey(path);
            setVariable(variable, Advapi32Util.registryGetValue(winRegPath.getRoot(), winRegPath.getKey(), winRegPath.getValue()));
        } catch (Win32Exception ex) {
            fail(message("registryGetValueErrorMessage", path));
        }
    }

    @TestStep("в реестре windows создан раздел \"${path}\"")
    public void registryCreateKey(String path) {
        boolean result = false;
        try {
            WinRegPath winRegPath = getHKey(path);
            result = Advapi32Util.registryKeyExists(winRegPath.getRoot(),winRegPath.getKey()+"\\"+winRegPath.getValue());
            // Раздел еще не существует
            if (!result) {
                result = Advapi32Util.registryCreateKey(winRegPath.getRoot(), winRegPath.getKey(), winRegPath.getValue());
            }
        } catch (Win32Exception ex) {
        }
        assertTrue(result, message("registryCreateKeyErrorMessage", path));
    }

    @TestStep("значение \"${path}\" в реестре windows заполнено строкой \"${value}\"")
    public void registrySetStringValue(String path, String value) {
        try {
            WinRegPath winRegPath = getHKey(path);
            Advapi32Util.registrySetStringValue(winRegPath.getRoot(),winRegPath.getKey(),winRegPath.getValue(),value);
        } catch (Exception ex) {
            fail(message("registrySetValue", path, value));
        }
    }

    @TestStep("значение \"${path}\" в реестре windows заполнено числом \"${value}\"")
    public void registrySetIntValue(String path, String value) {
        try {
            WinRegPath winRegPath = getHKey(path);
            Advapi32Util.registrySetIntValue(winRegPath.getRoot(),winRegPath.getKey(),winRegPath.getValue(),Integer.valueOf(value));
        } catch (Exception ex) {
            fail(message("registrySetValue", path, value));
        }
    }

    @TestStep("значение \"${path}\" в реестре windows заполнено большим числом \"${value}\"")
    public void registrySetLongValue(String path, String value) {
        try {
            WinRegPath winRegPath = getHKey(path);
            Advapi32Util.registrySetLongValue(winRegPath.getRoot(),winRegPath.getKey(),winRegPath.getValue(),Long.valueOf(value));
        } catch (Exception ex) {
            fail(message("registrySetValue", path, value));
        }
    }

    private static class WinRegPath {
        @Getter @Setter
        private WinReg.HKEY root;

        @Getter @Setter
        private String key;

        @Getter @Setter
        private String value;
    }

    private Pattern patternHKey=Pattern.compile("([\\w_]*)\\\\(.*)\\\\(.*)");

    private WinRegPath getHKey(String path) {
        WinRegPath result = null;

        Matcher matcher = patternHKey.matcher(path + "");
        if(matcher.find()) {
            result = new WinRegPath();
            result.setKey(matcher.group(2));
            result.setValue(matcher.group(3));
            switch (matcher.group(1).toUpperCase()) {
                case "HKEY_CLASSES_ROOT":
                    result.setRoot(HKEY_CLASSES_ROOT);
                    break;
                case "HKEY_CURRENT_USER":
                    result.setRoot(HKEY_CURRENT_USER);
                    break;
                case "HKEY_LOCAL_MACHINE":
                    result.setRoot(HKEY_LOCAL_MACHINE);
                    break;
                case "HKEY_USERS":
                    result.setRoot(HKEY_USERS);
                    break;
                case "HKEY_CURRENT_CONFIG":
                    result.setRoot(HKEY_CURRENT_CONFIG);
                    break;
                default:
                    result = null;
            }
        }
        assertNotNull(result, message("registryIncorrectKeyAssertMessage", path));
        return result;
    }
}
