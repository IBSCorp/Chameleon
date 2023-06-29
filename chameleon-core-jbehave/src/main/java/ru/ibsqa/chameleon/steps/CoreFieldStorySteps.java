package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.compare.ICompareManager;
import ru.ibsqa.chameleon.converters.Variable;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CoreFieldStorySteps extends AbstractSteps {

    @Autowired
    private CoreFieldSteps fieldSteps;

    @Autowired
    private CoreVariableSteps variableSteps;

    @Autowired
    private ICompareManager compareManager;

    @When("поле \"$fieldName\" заполняется значением \"$value\"")
    public void  stepFillField(String fieldName, Variable value){
        flow(()->
                fieldSteps.fillField(fieldName, value.getValue())
        );
    }

    @When("заполняются поля: $fields")
    public void stepFillField(ExamplesTable fields) {
        flow(()-> {
            for (Map<String, String> row : fields.getRows()) {
                String field = row.get("field");
                String value = row.get("value");
                value = evalVariable(value);
                fieldSteps.fillField(field, value);
            }
        });
    }

    @When("заполняются переменные: $variables")
    public void stepFillVariable(ExamplesTable variables) {
        flow(()-> {
            for (Map<String, String> row : variables.getRows()) {
                String variable = row.get("variable");
                String value = row.get("value");
                value = evalVariable(value);
                setVariable(variable, value);
            }
        });
    }

    @When("заполняются поля из таблицы <fields>")
    public void stepFillFieldFromParameters(Variable fields){
        flow(()-> {
            String[] fieldsArr = fields.getValue().split("~");
            for (String field : fieldsArr){
                String[] fieldAndValue = field.split("=");
                String fld = fieldAndValue[0];
                String value = "";
                if (fieldAndValue.length > 1){
                    value = fieldAndValue[1];
                }
                fieldSteps.fillField(fld, evalVariable(value));
            }
        });
    }

    @When("заполняются переменные из таблицы <variables>")
    public void stepFillVariableFromParameters(Variable variables){
        flow(()-> {
            String[] variablesArr = variables.getValue().split("~");
            for (String variable : variablesArr){
                String[] variableAndValue = variable.split("=");
                String var = variableAndValue[0];
                String value = "";
                if (variableAndValue.length > 1){
                    value = variableAndValue[1];
                }
                setVariable(var, evalVariable(value));
            }
        });
    }

    @Then("значение поля \"$fieldName\" равно \"$value\"")
    public void stepCheckFieldValue(String fieldName, Variable value){
        flow(()->
                fieldSteps.checkFieldValue(fieldName, compareManager.defaultOperator(), value.getValue())
        );
    }

    @Then("значение выражения \"$variable\" равно \"$value\"")
    public void stepCheckVariableValue(Variable variable, Variable value){
        flow(()->
                variableSteps.checkExpressionValue(variable.getValue(), compareManager.defaultOperator(), value.getValue())
        );
    }

    @Then("значение поля \"$fieldName\" не равно \"$value\"")
    public void stepCheckFieldNotValue(String fieldName, Variable value){
        flow(()->
                fieldSteps.checkFieldValue(fieldName, "не равно", value.getValue())
        );
    }

    @Then("значение выражения \"$variable\" не равно \"$value\"")
    public void stepCheckVariableNotValue(String variable, Variable value){
        flow(()->
                variableSteps.checkExpressionValue(variable, "не равно", value.getValue())
        );
    }

    @Then("значение поля \"$fieldName\" больше \"$value\"")
    public void stepCheckFieldValueGreater(String fieldName, Variable value){
        flow(()->
                fieldSteps.checkFieldValue(fieldName, "больше", value.getValue())
        );
    }

    @Then("значение выражения \"$variable\" больше \"$value\"")
    public void stepCheckVariableValueGreater(Variable variable, Variable value){
        flow(()->
                variableSteps.checkExpressionValue(variable.getValue(), "больше", value.getValue())
        );
    }

    @Then("значение поля \"$fieldName\" больше или равно \"$value\"")
    public void stepCheckFieldValueGreaterOrEqual(String fieldName, Variable value){
        flow(()->
                fieldSteps.checkFieldValue(fieldName, "больше или равно", value.getValue())
        );
    }

    @Then("значение выражения \"$variable\" больше или равно \"$value\"")
    public void stepCheckVariableValueGreaterOrEqual(Variable variable, Variable value){
        flow(()->
                variableSteps.checkExpressionValue(variable.getValue(), "больше или равно", value.getValue())
        );
    }

    @Then("значение поля \"$fieldName\" меньше \"$value\"")
    public void stepCheckFieldValueLower(String fieldName, Variable value){
        flow(()->
                fieldSteps.checkFieldValue(fieldName, "меньше", value.getValue())
        );
    }

    @Then("значение выражения \"$variable\" меньше \"$value\"")
    public void stepCheckVariableValueLower(Variable variable, Variable value){
        flow(()->
                variableSteps.checkExpressionValue(variable.getValue(), "меньше", value.getValue())
        );
    }

    @Then("значение поля \"$fieldName\" меньше или равно \"$value\"")
    public void stepCheckFieldValueLowerOrEqual(String fieldName, Variable value){
        flow(()->
                fieldSteps.checkFieldValue(fieldName, "меньше или равно", value.getValue())
        );
    }

    @Then("значение выражения \"$variable\" меньше или равно \"$value\"")
    public void stepCheckVariableValueLowerOrEqual(Variable variable, Variable value){
        flow(()->
                variableSteps.checkExpressionValue(variable.getValue(), "меньше или равно", value.getValue())
        );
    }

    @Then("значение поля \"$fieldName\" содержит значение \"$value\"")
    public void stepCheckFieldContainsValue(String fieldName, Variable value){
        flow(() -> fieldSteps.checkFieldValue(fieldName, "содержит значение", value.getValue()));
    }

    @Then("значение выражения \"$variable\" содержит значение \"$value\"")
    public void stepCheckVariableContainsValue(Variable variable, Variable value){
        flow(()->
                variableSteps.checkExpressionValue(variable.getValue(), "содержит значение", value.getValue())
        );
    }

    @Then("значения полей: $fields")
    public void stepCheckFieldValue(ExamplesTable fields) {
        flow(()-> {
            for (Map<String, String> row : fields.getRows()) {
                String field = row.get("field");
                String value = evalVariable(row.get("value"));
                fieldSteps.checkFieldValue(field, compareManager.defaultOperator(), value);
            }
        });
    }

    @Then("значения выражений: $variables")
    public void stepCheckVariableValue(ExamplesTable variables) {
        flow(()-> {
            for (Map<String, String> row : variables.getRows()) {
                String variable = row.get("variable");
                String value = row.get("value");
                variableSteps.checkExpressionValue(variable, compareManager.defaultOperator(), value);
            }
        });
    }

    @Then("значение поля \"$fieldName\" начинается с \"$value\"")
    public void stepFieldStartsWith(String fieldName, Variable value){
        flow(()->
                fieldSteps.checkFieldValue(fieldName, "начинается с", value.getValue())
        );
    }

    @Then("значение выражения \"$variable\" начинается с \"$value\"")
    public void stepVariableStartsWith(Variable variable, Variable value){
        flow(()->
                variableSteps.checkExpressionValue(variable.getValue(), "начинается с", value.getValue())
        );
    }

    @When("в переменной \"$variable\" сохранено значение поля \"$fieldName\"")
    public void stepSaveVariable(String variable, String fieldName) {
        flow(()->
                setVariable(variable, fieldSteps.getFieldValue(fieldName))
        );
    }

    @When("в переменной \"$variable\" сохранено значение поля \"$fieldName\" начиная с индекса \"$index\"")
    public void stepSaveVariableSubStr(String variable, String fieldName, int index) {
        flow(()->
                setVariable(variable, fieldSteps.getFieldValue(fieldName).substring(index))
        );
    }

    @When("в переменной \"$variable\" сохранено значение поля \"$fieldName\" выбранное по регулярному выражению \"$pattern\"")
    public void stepCreateVariableByPattern(String variable, String fieldName, String pattern) {
        flow(()-> {
            String value = fieldSteps.getFieldValue(fieldName);
            Pattern regexp = Pattern.compile(pattern);
            Matcher m = regexp.matcher(value);
            if (m.find()) {
                setVariable(variable, m.group(1));
            }
        });
    }

    @When("поле \"$fieldName\" очищено")
    public void stepClearField(String fieldName){
        flow(()->
                fieldSteps.clearField(fieldName)
        );
    }

    @When("очищены поля: $fields")
    public void stepClearField(ExamplesTable fields) {
        flow(()-> {
            for (Map<String, String> row : fields.getRows()) {
                String field = row.get("field");
                fieldSteps.clearField(field);
            }
        });
    }

    @Then("поле \"$fieldName\" присутствует")
    public void checkFieldExists(String fieldName) {
        flow(()->
                fieldSteps.checkFieldExists(fieldName)
        );
    }

    @Then("следующие поля присутствуют: $fields")
    public void checkFieldExists(ExamplesTable fields) {
        flow(()-> {
            for (Map<String, String> row : fields.getRows()) {
                String field = row.get("field");
                fieldSteps.checkFieldExists(field);
            }
        });
    }

    @Then("поле \"$fieldName\" отсутствует")
    public void checkFieldNotExists(String fieldName) {
        flow(()->
                fieldSteps.checkFieldNotExists(fieldName)
        );
    }

    @Then("следующие поля отсутствуют: $fields")
    public void checkFieldNotExists(ExamplesTable fields) {
        flow(()-> {
            for (Map<String, String> row : fields.getRows()) {
                String field = row.get("field");
                fieldSteps.checkFieldNotExists(field);
            }
        });
    }

    @Then("длинна значения поля \"$fieldName\" не превышает \"$maxLength\"")
    public void checkFieldMaxLength(String fieldName, Variable maxLength){
        flow(()-> {
            fieldSteps.checkFieldValue(fieldName, "по длине меньше", maxLength.getValue());
        });
    }

    @Then("длинна значения поля \"$fieldName\" не меньше \"$minLength\"")
    public void checkFieldMinLength(String fieldName, Variable minLength){
        flow(()-> {
            fieldSteps.checkFieldValue(fieldName, "по длине больше", minLength.getValue());
        });
    }

}
