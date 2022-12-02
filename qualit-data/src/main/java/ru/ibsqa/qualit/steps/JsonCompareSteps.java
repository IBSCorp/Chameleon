package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.context.IContextExplorer;
import ru.ibsqa.qualit.elements.data.IFacadeDataExtractableField;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.json.utils.DataUtils;
import ru.ibsqa.qualit.reporter.TestAttachment;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Шаги для сравнения фрагментов JSON
 */
@Component
public class JsonCompareSteps extends AbstractSteps {

    @Autowired
    private IContextExplorer contextExplorer;

    @TestStep("json \"${actual}\" совпадает с \"${expected}\"")
    public void jsonEquals(String actual, String expected) {
        TransformResult actualJson = getJson(actual);
        TransformResult expectedJson = getJson(expected);
        try {
            attachExpected(expectedJson.getJsonPretty());
            attachActual(actualJson.getJsonPretty());
            JSONAssert.assertEquals(
                    message("jsonNotEqualsAssertMessage", actualJson.getMessage(getLocaleManager(), 1), expectedJson.getMessage(getLocaleManager(), 2)),
                    expectedJson.getValue(), actualJson.getValue(), JSONCompareMode.NON_EXTENSIBLE);
        } catch (JSONException e) {
            fail(e.getMessage());
        }
    }

    @TestStep("json \"${actual}\" содержит \"${expected}\"")
    public void jsonExtends(String actual, String expected) {
        TransformResult actualJson = getJson(actual);
        TransformResult expectedJson = getJson(expected);
        try {
            attachExpected(expectedJson.getJsonPretty());
            attachActual(actualJson.getJsonPretty());
            JSONAssert.assertEquals(
                    message("jsonNotExtendsAssertMessage", actualJson.getMessage(getLocaleManager(), 1), expectedJson.getMessage(getLocaleManager(), 2)),
                    expectedJson.getValue(), actualJson.getValue(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            fail(e.getMessage());
        }
    }

    @TestStep("json \"${actual}\" совпадает с \"${expected}\", порядок в массивах совпадает")
    public void jsonEqualsAndOrder(String actual, String expected) {
        TransformResult actualJson = getJson(actual);
        TransformResult expectedJson = getJson(expected);
        try {
            attachExpected(expectedJson.getJsonPretty());
            attachActual(actualJson.getJsonPretty());
            JSONAssert.assertEquals(
                    message("jsonNotEqualsAssertMessage", actualJson.getMessage(getLocaleManager(), 1), expectedJson.getMessage(getLocaleManager(), 2)),
                    expectedJson.getValue(), actualJson.getValue(), JSONCompareMode.STRICT);
        } catch (JSONException e) {
            fail(e.getMessage());
        }
    }

    @TestStep("json \"${actual}\" содержит \"${expected}\", порядок в массивах совпадает")
    public void jsonExtendsAndOrder(String actual, String expected) {
        TransformResult actualJson = getJson(actual);
        TransformResult expectedJson = getJson(expected);
        try {
            attachExpected(expectedJson.getJsonPretty());
            attachActual(actualJson.getJsonPretty());
            JSONAssert.assertEquals(
                    message("jsonNotExtendsAssertMessage", actualJson.getMessage(getLocaleManager(), 1), expectedJson.getMessage(getLocaleManager(), 2)),
                    expectedJson.getValue(), actualJson.getValue(), JSONCompareMode.STRICT_ORDER);
        } catch (JSONException e) {
            fail(e.getMessage());
        }
    }

    @TestStep("json \"${actual}\" не совпадает с \"${expected}\"")
    public void jsonNotEquals(String actual, String expected) {
        TransformResult actualJson = getJson(actual);
        TransformResult expectedJson = getJson(expected);
        try {
            attachExpected(expectedJson.getJsonPretty());
            attachActual(actualJson.getJsonPretty());
            JSONAssert.assertNotEquals(
                    message("jsonEqualsAssertMessage", actualJson.getMessage(getLocaleManager(), 1), expectedJson.getMessage(getLocaleManager(), 2)),
                    expectedJson.getValue(), actualJson.getValue(), JSONCompareMode.NON_EXTENSIBLE);
        } catch (JSONException e) {
            fail(e.getMessage());
        }
    }

    @TestStep("json \"${actual}\" не содержит \"${expected}\"")
    public void jsonNotExtends(String actual, String expected) {
        TransformResult actualJson = getJson(actual);
        TransformResult expectedJson = getJson(expected);
        try {
            attachExpected(expectedJson.getJsonPretty());
            attachActual(actualJson.getJsonPretty());
            JSONAssert.assertNotEquals(
                    message("jsonExtendsAssertMessage", actualJson.getMessage(getLocaleManager(), 1), expectedJson.getMessage(getLocaleManager(), 2)),
                    expectedJson.getValue(), actualJson.getValue(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            fail(e.getMessage());
        }
    }

    private TransformResult getJson(String value) {
        TransformResult result = new TransformResult();
        if (DataUtils.isJSON(value)) {
            result.setValue(value);
        } else if (SpringUtils.isClassPathResource(value)) {
            result.setResourceName(value);
            result.setValue(DataUtils.getDataAsString(value));
        } else {
            IFacadeDataExtractableField field = contextExplorer.pickElement(value, IFacadeDataExtractableField.class).getElement();
            result.setFieldName(value);
            result.setJsonPretty(field.getDataPretty());
            result.setValue(field.getDataValue());
        }
        return result;
    }

    @ToString
    private static class TransformResult {

        @Getter @Setter
        private String fieldName = null;

        @Getter @Setter
        private String resourceName = null;

        @Getter @Setter
        private String value;

        @Setter
        private String jsonPretty;

        public String getJsonPretty() {
            return (null != jsonPretty) ? jsonPretty : value;
        }

        public String getMessage(ILocaleManager localeManager, int index) {
            String message = "";
            if (null != getFieldName()) {
                message = localeManager.getMessage("jsonFieldMessage", getFieldName());
            } else if (null != getResourceName()) {
                message = localeManager.getMessage("jsonResourceMessage", getResourceName());
            } else {
                message = localeManager.getMessage("jsonFragmentMessage", getValue());
            }
            if (null != message && message.length()>1 && 1 == index) {
                message = message.substring(0, 1).toUpperCase() + message.substring(1);
            }
            return message;
        }

    }

    @TestAttachment(value = "Проверяемый json", mimeType = "text/plain")
    private String attachActual(String json) {
        return json;
    }

    @TestAttachment(value = "Ожидаемый json", mimeType = "text/plain")
    private String attachExpected(String json) {
        return json;
    }

}
