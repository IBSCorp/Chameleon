package ru.ibsqa.qualit.steps;

import org.apache.commons.lang3.StringUtils;
import ru.ibsqa.qualit.context.Context;
import ru.ibsqa.qualit.converters.FieldOperatorValueTable;
import ru.ibsqa.qualit.converters.FieldTable;
import ru.ibsqa.qualit.converters.FieldValueTable;
import ru.ibsqa.qualit.steps.roles.*;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoreFieldStorySteps extends AbstractSteps {

    @Autowired
    private CoreFieldSteps fieldSteps;

    @DebugPluginAction
    @StepDescription(action = "UI->Элементы->Действия->Заполнить поле"
            , subAction = "Заполнить поле"
            , parameters = {"field - наименование поля", "value - значение"})
    @Когда("^поле \"([^\"]*)\" заполняется значением \"(.*)\"$")
    public void stepFillField(
            @Write String field,
            @Value String value
    ) {
        flow(()->
                fieldSteps.fillField(field, value)
        );
    }

    @StepDescription(action = "UI->Элементы->Действия->Заполнить поле"
            , subAction = "Заполнить поле"
            , multiple = true
            , parameters = {"fields - наименование полей"})
    @Когда("^заполняются поля:$")
    public void stepFillFields(
            @Write("field") @Value("value") List<FieldValueTable> fields
    ) {
        flow(()-> {
            for (FieldValueTable fieldValue : fields) {
                fieldSteps.fillField(fieldValue.getField(), Optional.ofNullable(evalVariable(fieldValue.getValue())).orElse(StringUtils.EMPTY));
            }
        });
    }

    @DebugPluginAction
    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Проверить значение поля"
            , parameters = {"field - наименование поля", "operator - соответствие чему проверять", "value - значение"})
    @Тогда("^значение поля \"([^\"]*)\" (равно|не равно|содержит значение|не содержит значение|начинается с|не начинается с|оканчивается на|не оканчивается на|соответствует|не соответствует|равно игнорируя регистр|не равно игнорируя регистр|равно игнорируя пробелы|не равно игнорируя пробелы|по длине равно|по длине не равно|по длине больше|по длине не меньше|по длине меньше|по длине не больше|больше|больше или равно|меньше|меньше или равно|раньше или равно|позже или равно|позже|раньше) \"(.*)\"$")
    public void stepCheckFieldValue(
            @Read String field,
            @Value CompareOperatorEnum operator,
            @Value String value) {
        flow(()->
                fieldSteps.checkFieldValue(field, operator, value)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Проверить значение поля"
            , multiple = true
            , parameters = {"fields - поля, операторы, значения"})
    @Тогда("^значения полей:$")
    public void stepCheckFieldsValue(
            @Read("field") @Value({"operator", "value"}) List<FieldOperatorValueTable> fields) {
        flow(()-> {
            for (FieldOperatorValueTable fieldValue : fields) {
                fieldSteps.checkFieldValue(
                        fieldValue.getField(),
                        Optional.ofNullable(fieldValue.getOperator()).orElse(CompareOperatorEnum.EQUALS),
                        Optional.ofNullable(fieldValue.getValue()).map(this::evalVariable).orElse(StringUtils.EMPTY)
                );
            }
        });
    }

    @StepDescription(action = "UI->Элементы->Действия->Сохранить значение поля"
            , subAction = "Сохранить значение поля"
            , parameters = {"variable - наименование переменной", "field - наименование поля"})
    @Тогда("^в переменной \"([^\"]*)\" сохранено значение поля \"([^\"]*)\"$")
    @Context(variables = {"variable"})
    public void stepSaveVariable(
            @Variable String variable,
            @Read String field
    ) {
        flow(()-> {
            String value = fieldSteps.getFieldValue(field);
            setVariable(variable, value);
        });
    }

    @Тогда("^в переменной \"([^\"]*)\" сохранено значение поля \"([^\"]*)\" начиная с индекса \"([^\"]*)\"$")
    @Context(variables = {"variable"})
    public void stepSaveVariableSubStr(
            @Read String field,
            @Variable String variable,
            @Value int index
    ) {
        flow(()-> {
            String value = fieldSteps.getFieldValue(field).substring(index);
            setVariable(variable, value);
        });
    }

    @Тогда("^в переменной \"([^\"]*)\" сохранено значение поля \"([^\"]*)\" выбранное по регулярному выражению \"([^\"]*)\"$")
    @Context(variables = {"var"})
    public void stepCreateVariableByPattern(
            @Variable String var,
            @Read String field,
            @Value String pattern
    ) {
        flow(()-> {
            String value = fieldSteps.getFieldValue(field);
            Pattern regexp = Pattern.compile(pattern);
            Matcher m = regexp.matcher(value);
            if (m.find()) {
                setVariable(var, m.group(1));
            }
        });
    }

    @StepDescription(action = "UI->Элементы->Действия->Сохранить значение поля"
            , subAction = "Сохранить значения полей"
            , parameters = {"fieldName - наименование поля", "variable - наименование переменной"})
    @Тогда("^значения полей сохранены в переменные:$")
    public void saveVariables(
            @Read("field") List<FieldTable> conditions) {
        flow(() -> {
            for (FieldTable fieldTable : conditions) {
                stepSaveVariable(fieldTable.getField(), fieldTable.getField().replaceAll("\\s", "_").replace("-", "_"));
            }
        });
    }

    @DebugPluginAction
    @StepDescription(action = "UI->Элементы->Действия->Очистить поле"
            , subAction = "Очистить поле"
            , parameters = {"field - наименование поля"})
    @Тогда("^поле \"([^\"]*)\" очищено$")
    public void stepClearField(
            @Write String field
    ) {
        flow(()->
                fieldSteps.clearField(field)
        );
    }

    @StepDescription(action = "UI->Элементы->Действия->Очистить поле"
            , subAction = "Очистить поле"
            , multiple = true
            , parameters = {"fields - список полей"})
    @Тогда("^очищены поля:$")
    public void stepClearFields(
            @Write("field") List<FieldTable> fields
    ) {
        flow(()-> {
            for (FieldTable fieldTable : fields) {
                fieldSteps.clearField(fieldTable.getField());
            }
        });
    }
    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поле присутствует"
            , parameters = {"field - наименование поля"})
    @Тогда("^поле \"([^\"]*)\" присутствует$")
    public void checkFieldExists(
            @Exists String field
    ) {
        flow(()->
                fieldSteps.checkFieldExists(field)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поле присутствует"
            , multiple = true
            , parameters = {"fields - список полей"})
    @Тогда("^следующие поля присутствуют:$")
    public void checkFieldsExists(
            @Exists("field") List<FieldTable> fields) {
        flow(()-> {
            for (FieldTable fieldTable : fields) {
                fieldSteps.checkFieldExists(fieldTable.getField());
            }
        });
    }

    @DebugPluginAction
    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поле отсутствует"
            , parameters = {"field - наименование поля"})
    @Тогда("^поле \"([^\"]*)\" отсутствует$")
    public void checkFieldNotExists(
            @Exists String field
    ) {
        flow(()->
                fieldSteps.checkFieldNotExists(field)
        );
    }

    @StepDescription(action = "UI->Элементы->Проверки->Проверить поле"
            , subAction = "Поле отсутствует"
            , multiple = true
            , parameters = {"fields - список полей"})
    @Тогда("^следующие поля отсутствуют:$")
    public void checkFieldsNotExists(
            @Exists("field") List<FieldTable> fields
    ) {
        flow(()-> {
            for (FieldTable fieldTable : fields) {
                fieldSteps.checkFieldNotExists(fieldTable.getField());
            }
        });
    }

}
