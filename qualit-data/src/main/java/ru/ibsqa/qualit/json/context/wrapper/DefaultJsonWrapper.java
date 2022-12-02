package ru.ibsqa.qualit.json.context.wrapper;

import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.json.context.CustomJsonSchemaValidationBundle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.msgsimple.load.MessageBundles;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import ru.ibsqa.qualit.reporter.TestAttachment;
import ru.ibsqa.qualit.steps.TestStep;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class DefaultJsonWrapper implements IDataWrapper {

    private final String CR  = "\r";
    private final String LF  = "\n";
    private final String CRLF  = CR + LF;

    private final JsonSchemaFactory schemaFactory = JsonSchemaFactory
            .newBuilder()
            .setValidationConfiguration(
                    ValidationConfiguration
                            .newBuilder()
                            .setValidationMessages(MessageBundles.getBundle(CustomJsonSchemaValidationBundle.class)
                            ).freeze()
            ).freeze();

    private final Configuration configuration = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider())
            .build();

    @Setter
    private String dataValue;

    /**
     * Получить фрагмент json
     * @return
     */
    @Override
    public String getDataValue(String locator) {
        if (null == locator || locator.isEmpty() || locator.equals("$")) {
            return dataValue;
        } else {
            return jsonObjectToJsonString(read(locator, Object.class), true);
        }
    }

    /**
     * Получить фрагмент json в форматированном виде
     * @return
     */
    @Override
    public String getDataPretty(String locator) {
        final int INDENT_SPACE = 2;
        String result = null;

        try {
            String value = getDataValue(locator);
            try {
                result = (new org.json.JSONObject(value)).toString(INDENT_SPACE);
            } catch (org.json.JSONException jsonException) {
                result = (new org.json.JSONArray(getDataValue(locator))).toString(INDENT_SPACE);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        if (null != result) {
            result = result.replace(LF,CRLF);
        }
        return result;
    }

    //TODO переделать, чтобы при каждом присвоении не пересобирался json
    private DocumentContext context() {
        return JsonPath.using(configuration).parse(dataValue);
    }

    /**
     * Определение, существует ли поле
     * @param locator
     * @return
     */
    @Override
    public boolean isExists(String locator) {
        try {
            Object result = context().read(locator);
            // Реализовать функцию проверки наличия в xpath запроса, пока проверяем по наличию подстроки "?(@."
            // В дальнейшем использовать эту же функцию в проверках при чтении скалярного значения (метод read)
            if (null != locator && locator.contains("?(@.") && result instanceof ArrayNode) {
                ArrayNode list = (ArrayNode) result;
                return list.size() > 0;
            }
            return true;
        } catch (InvalidPathException e) {
            return false;
        }
    }

    /**
     * Проверка наличия поля
     * @param locator
     * @throws PathException
     */
    private void check(String locator) throws PathException {
        if (!isExists(locator)) {
            throw new PathException(ILocaleManager.message("jPathNotFoundErrorMessage", locator));
        }
    }

    /**
     * Установка значения существующего поля
     * @param locator
     * @param value
     * @throws PathException
     */
    @Override
    public void set(String locator, Object value) throws PathException {
        // Проверим, что есть такой путь
        check(locator);
        dataValue = context().set(locator, value).jsonString();
    }

    /**
     * Добавление нового поля
     * @param locator
     * @param value
     * @throws PathException
     */
    @Override
    public void create(String locator, Object value) {

        String jsonParentPath = "";
        String key = "";

        Pattern patternJsonKey= Pattern.compile("(.*)\\.(.*)");

        Matcher matcher = patternJsonKey.matcher(locator + "");
        if(matcher.find()) {
            jsonParentPath = matcher.group(1);
            key = matcher.group(2);
            dataValue = context().put(jsonParentPath, key, value).jsonString();
        } else {
            fail(ILocaleManager.message("matchlocatorAndKeyError", locator));
        }

    }

    /**
     * Удаление существующего поля
     * @param locator
     * @throws PathException
     */
    @Override
    public void delete(String locator) throws PathException {
        // Проверим, что есть такой путь
        check(locator);
        dataValue = context().delete(locator).jsonString();
    }

    @Override
    public void add(String locator, Object value) {
        // Проверим, что есть такой путь
        check(locator);
        dataValue = context().add(locator, value).jsonString();
    }

    /**
     * Считываем значение поля
     * @param locator
     * @param type
     * @param <T>
     * @return
     * @throws PathException
     */
    @Override
    public <T> T read(String locator, Class<T> type) throws PathException {
        try {
            Object object = context().read(locator, Object.class);

            if (object instanceof List && !List.class.isAssignableFrom(type) && Object.class != type) {
                List list = (List)object;
                if (0 == list.size()) {
                    throw new PathException(ILocaleManager.message("jPathNotFoundErrorMessage", locator));
                }
                if (1 < list.size()) {
                    throw new PathException(ILocaleManager.message("jPathNotUniqueErrorMessage", locator));
                }
                object = list.get(0);
            }
            if (null == object) {
                return null;
            }
            if (String.class == type) {
                return (T)jsonObjectToJsonString(object,false);
            } else {
                return type.cast(object);
            }


        } catch (PathNotFoundException e) {
            throw new PathException(ILocaleManager.message("jPathNotFoundErrorMessage", locator));
        }
    }

    /**
     * Превращаем прочитанный объект в корректный json, если он не скалярный
     * @param object
     * @param wrap
     * @return
     */
    private String jsonObjectToJsonString(Object object, boolean wrap) {
        if (null == object) {
            return null;
        }

        if (object instanceof Map) {
            String result = "";
            for (Map.Entry<String,Object> entry : ((Map<String,Object>)object).entrySet() ) {
                if (!result.isEmpty()) {
                    result += ",";
                }
                result += String.format("%s:%s", jsonObjectToJsonString(entry.getKey(),true), jsonObjectToJsonString(entry.getValue(), true));
            }
            return String.format("{%s}", result);
        } else if (object instanceof List) {
            return String.format("[%s]", ((List) object).stream().map(
                    item -> jsonObjectToJsonString(item, true)
            ).collect(Collectors.joining(",")));
        } else {
            if (wrap && (object instanceof String)) {
                return String.format("\"%s\"", object.toString()
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                );
            } else {
                return object.toString();
            }
        }

    }

    /**
     * Валидируем json по схеме
     * @param schema
     */
    @Override
    public void validateSchema(String schema) {

        validateStep();
        attachSchema(schema);

        try {
            JsonSchema jsonSchema = schemaFactory.getJsonSchema(JsonLoader.fromString(schema));
            JsonNode nodeValue = JsonLoader.fromString(dataValue);
            assertReport(jsonSchema.validate(nodeValue, true));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } catch (ProcessingException e) {
            log.error(e.getMessage(), e);
            fail(ILocaleManager.message("invalidJsonSchemaErrorMessage")+CRLF+e.getMessage());
        }
    }

    @TestStep("валидация данных по схеме")
    private void validateStep() {
    }

    @TestAttachment(value = "Json-схема", mimeType = "text/plain")
    private String attachSchema(String schema) {
        return schema;
    }

    /**
     * Вывод сообщений об ошибках валидации
     */
    private void assertReport(ProcessingReport report) {

        if (!report.isSuccess()) {
            StringBuilder message = new StringBuilder();
            message.append("");
            report.forEach(action ->
                    message.append(message.toString().isEmpty()?"":";"+CRLF).append(action.getMessage())
            );
            fail(message.toString());
        }
    }

    @Override
    public long getDataLength(String locator) {
        Object object = read(locator, Object.class);
        if (object instanceof List) {
            return ((List) object).stream().count();
        } else if (object instanceof Map) {
            return ((Map) object).keySet().size();
        }

        return -1;
    }

}
