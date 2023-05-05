package ru.ibsqa.qualit.evaluate;

import org.junit.jupiter.api.Assertions;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.storage.IVariableScope;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.VerRsrc;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Возвращает версию файла в windows (для exe, dll, ...)
 * Пример использования:
 *      #version{C:\Program Files (x86)\RussianPost\PostOffice\config\config01.exe}
 */
@Component
@Evaluator(value = {
        "#file{имя_файла}",
        "#file{C:\\Program Files (x86)\\RussianPost\\PostOffice\\config\\config01.exe}"
}, priority = ConfigurationPriority.LOW)
@Slf4j
public class EvaluatorVersionImpl extends AbstractEvaluator {

    @Override
    protected String getPlaceHolderName() {
        return "version";
    }

    @Override
    protected String evalExpression(IVariableScope variableScope, String... args) {

        String filePath = args[0];

        try {

            IntByReference dwDummy = new IntByReference();
            dwDummy.setValue(0);

            int versionlength =
                    com.sun.jna.platform.win32.Version.INSTANCE.GetFileVersionInfoSize(
                            filePath, dwDummy);

            byte[] bufferarray = new byte[versionlength];
            Pointer lpData = new Memory(bufferarray.length);
            PointerByReference lplpBuffer = new PointerByReference();
            IntByReference puLen = new IntByReference();

            boolean fileInfoResult =
                    com.sun.jna.platform.win32.Version.INSTANCE.GetFileVersionInfo(
                            filePath, 0, versionlength, lpData);

            boolean verQueryVal =
                    com.sun.jna.platform.win32.Version.INSTANCE.VerQueryValue(
                            lpData, "\\", lplpBuffer, puLen);

            VerRsrc.VS_FIXEDFILEINFO lplpBufStructure = new VerRsrc.VS_FIXEDFILEINFO(lplpBuffer.getValue());
            lplpBufStructure.read();

            int v1 = (lplpBufStructure.dwFileVersionMS).intValue() >> 16;
            int v2 = (lplpBufStructure.dwFileVersionMS).intValue() & 0xffff;
            int v3 = (lplpBufStructure.dwFileVersionLS).intValue() >> 16;
            int v4 = (lplpBufStructure.dwFileVersionLS).intValue() & 0xffff;

            return String.valueOf(v1) + "." +
                    String.valueOf(v2) + "." +
                    String.valueOf(v3) + "." +
                    String.valueOf(v4);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Assertions.fail(String.format(localeManager.getMessage("getFileVersionErrorMessage"),filePath));
        }
        return null;
    }

    @Override
    protected boolean isMultiArgs() {
        return false;
    }
}