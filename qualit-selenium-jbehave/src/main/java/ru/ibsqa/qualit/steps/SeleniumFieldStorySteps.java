package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.converters.Variable;
import ru.ibsqa.qualit.selenium.enums.KeyEnum;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SeleniumFieldStorySteps extends AbstractSteps {

    @Autowired
    private SeleniumFieldSteps fieldSteps;

    @When("поле \"$fieldName\" заполняется значением \"$value\" и нажимается ENTER")
    public void  stepFillFieldAndPressEnter(Variable fieldName, Variable value){
        flow(()-> {
            if (fieldName.getValue().isEmpty()) {
                return;
            }
            fieldSteps.fillField(fieldName.getValue(), value.getValue());
            fieldSteps.pressKeyField(fieldName.getValue(), KeyEnum.ENTER);
        });
    }

    @Then("значение подсказки для поля \"$fieldName\" равно \"$value\"")
    public void checkFieldPlaceholder(Variable fieldName, Variable expected){
        flow(()->
                fieldSteps.checkFieldPlaceholder(fieldName.getValue(), CompareOperatorEnum.EQUALS, expected.getValue())
        );
    }

    @When("в поле \"$fieldName\" выполнено нажатие клавиши \"$key\"")
    public void pressKeyField(Variable fieldName, KeyEnum key){
        flow(()-> {
            if (fieldName.getValue().isEmpty()) {
                return;
            }
            fieldSteps.pressKeyField(fieldName.getValue(), key);
        });
    }

    @When("выполнено нажатие на \"$fieldName\"")
    public void stepClickField(Variable fieldName){
        flow(()-> {
            if (fieldName.getValue().isEmpty()) {
                return;
            }
            fieldSteps.clickField(fieldName.getValue());
        });
    }

    @When("выполнено нажатие на \"$fieldName\" \"$amount\" {раз|раза}")
    public void stepClickFieldNTimes(Variable fieldName, Variable amount){
        flow(()-> {
            if (fieldName.getValue().isEmpty()) {
                return;
            }
            for (int i = 0; i < Integer.parseInt(amount.getValue()); i++) {
                fieldSteps.clickField(fieldName.getValue());
            }
        });
    }

    @When("нажатием на {поле|кнопку|ссылку} \"$fieldName\" {загружена|загружен} {страница|виджет|диалог|плагин|папка} \"$page\"")
    public void stepClickFieldAndPageLoaded(Variable fieldName, Variable page){
        flow(()->
                fieldSteps.clickFieldAndPageLoaded(fieldName.getValue(), page.getValue())
        );
    }

    @Then("поле \"$fieldName\" видимо")
    public void stepFieldIsDisplayed1(Variable fieldName){
        flow(()->
                fieldSteps.fieldIsDisplayed(fieldName.getValue())
        );
    }

    @Then("{ссылка|кнопка} \"$fieldName\" видима")
    public void stepFieldIsDisplayed2(Variable fieldName){
        flow(()->
                fieldSteps.fieldIsDisplayed(fieldName.getValue())
        );
    }

    @Then("следующие {поля|ссылки|кнопки} видимы: $fields")
    public void stepFieldsIsDisplayed(ExamplesTable fields){
        flow(()-> {
            for (Map<String, String> row : fields.getRows()) {
                String field = evalVariable(row.get("field"));
                fieldSteps.fieldIsDisplayed(field);
            }
        });
    }

    @Then("{ссылка|кнопка} \"$fieldName\" отсутствует")
    public void stepFieldIsNotExist(Variable fieldName){
        flow(()->
                fieldSteps.fieldIsNotExist(fieldName.getValue())
        );
    }

    @Then("следующие {ссылки|кнопки} отсутствуют: $fields")
    public void stepFieldsIsNotExist(ExamplesTable fields){
        flow(()-> {
            for (Map<String, String> row : fields.getRows()) {
                String field = evalVariable(row.get("field"));
                fieldSteps.fieldIsNotExist(field);
            }
        });
    }

    @Then("поле \"$fieldName\" активно")
    public void stepFieldIsEnabled1(Variable fieldName){
        flow(()->
                fieldSteps.fieldIsEnabled(fieldName.getValue())
        );
    }

    @Then("кнопка \"$fieldName\" активна")
    public void stepFieldIsEnabled2(Variable fieldName){
        flow(()->
                fieldSteps.fieldIsEnabled(fieldName.getValue())
        );
    }

    @Then("следующие {поля|кнопки} активны: $fields")
    public void stepFieldsIsEnabled(ExamplesTable fields){
        flow(()-> {
            for (Map<String, String> row : fields.getRows()) {
                String field = evalVariable(row.get("field"));
                fieldSteps.fieldIsEnabled(field);
            }
        });
    }

    @Then("поле \"$fieldName\" неактивно")
    public void stepFieldIsDisabled1(Variable fieldName){
        flow(()->
                fieldSteps.fieldIsDisabled(fieldName.getValue())
        );
    }

    @Then("кнопка \"$fieldName\" неактивна")
    public void stepFieldIsDisabled2(Variable fieldName){
        flow(()->
                fieldSteps.fieldIsDisabled(fieldName.getValue())
        );
    }

    @Then("следующие {поля|кнопки} неактивны: $fields")
    public void stepFieldsIsDisabled(ExamplesTable fields){
        flow(()-> {
            for (Map<String, String> row : fields.getRows()) {
                String field = evalVariable(row.get("field"));
                fieldSteps.fieldIsDisabled(field);
            }
        });
    }

    @Then("поле \"$fieldName\" редактируемо")
    public void stepFieldIsEditable(Variable fieldName){
        flow(()->
                fieldSteps.fieldIsEditable(fieldName.getValue())
        );
    }

    @Then("следующие поля редактируемы: $fields")
    public void stepFieldsIsEditable(ExamplesTable fields){
        flow(()-> {
            for (Map<String, String> row : fields.getRows()) {
                String field = evalVariable(row.get("field"));
                fieldSteps.fieldIsEditable(field);
            }
        });
    }

    @Then("поле \"$fieldName\" нередактируемо")
    public void stepFieldIsNotEnabled(Variable fieldName){
        flow(()->
                fieldSteps.fieldIsNotEditable(fieldName.getValue())
        );
    }

    @Then("следующие поля нередактируемы: $fields")
    public void stepFieldsIsNotEditable(ExamplesTable fields){
        flow(()-> {
            for (Map<String, String> row : fields.getRows()) {
                String field = evalVariable(row.get("field"));
                fieldSteps.fieldIsNotEditable(field);
            }
        });
    }

    @Then("значение {ссылки|кнопки} \"$fieldName\" равно \"$value\"")
    public void stepCheckValue(Variable fieldName, Variable value){
        flow(()->
                fieldSteps.checkFieldValue(fieldName.getValue(), CompareOperatorEnum.EQUALS, value.getValue())
        );
    }

    @Then("значение {ссылки|кнопки} \"$fieldName\" не равно \"$value\"")
    public void stepCheckNotValue(Variable fieldName, Variable value){
        flow(()->
                fieldSteps.checkFieldValue(fieldName.getValue(), CompareOperatorEnum.NOT_EQUALS, value.getValue())
        );
    }

    @Then("значение {ссылки|кнопки} \"$fieldName\" содержит значение \"$value\"")
    public void stepCheckContainsValue(Variable fieldName, Variable value){
        flow(()->
                fieldSteps.checkFieldValue(fieldName.getValue(), CompareOperatorEnum.CONTAINS, value.getValue())
        );
    }

    @Then("ожидается исчезновение поля \"$fieldName\" в течение \"$seconds\" {секунд|секунды}")
    public void stepWaitFieldInVisible(Variable fieldName, Variable seconds){
        flow(()->
                fieldSteps.waitFieldInVisible(fieldName.getValue(), Integer.parseInt(seconds.getValue()))
        );
    }

    @Then("ожидается появление поля \"$fieldName\" в течение \"$seconds\" {секунд|секунды}")
    public void stepWaitFieldVisible(Variable fieldName, Variable seconds){
        flow(()->
                fieldSteps.waitFieldVisible(fieldName.getValue(), Integer.parseInt(seconds.getValue()))
        );
    }

    @Then("ожидается что {поле|кнопка} \"$fieldName\" станет {активным|активной} в течение \"$seconds\" {секунд|секунды}")
    public void stepWaitFieldIsEnabled(Variable fieldName, Variable seconds){
        flow(()->
                fieldSteps.waitFieldIsEnabled(fieldName.getValue(), Integer.parseInt(seconds.getValue()))
        );
    }

    @Then("ожидается что {поле|кнопка} \"$fieldName\" станет {неактивным|неактивной} в течение \"$seconds\" {секунд|секунды}")
    public void stepWaitFieldIsDisabled(Variable fieldName, Variable seconds){
        flow(()->
                fieldSteps.waitFieldIsDisabled(fieldName.getValue(), Integer.parseInt(seconds.getValue()))
        );
    }

    @Then("значение атрибута \"$attribute\" поля \"$fieldName\" равно \"$value\"")
    public void stepCheckFieldAttribute(Variable attribute, Variable fieldName, Variable value) {
        flow(()-> {
            fieldSteps.checkFieldAttribute(attribute.getValue(), fieldName.getValue(), CompareOperatorEnum.EQUALS, value.getValue());
            /*
            String currentAttribute = fieldSteps.getFieldAttribute(fieldName.getValue(), attribute.getValue());
            if (currentAttribute == null)
                currentAttribute = "";
            assertEquals(String.format("Значение атрибута [%s] поля [%s] не равно [%s]", attribute.getValue(), fieldName.getValue(), value.getValue()), value.getValue(), currentAttribute);
            */
        });
    }

    @Then("значение атрибута \"$attribute\" поля \"$fieldName\" содержит \"$value\"")
    public void checkFieldAttributeContains(Variable attribute, Variable fieldName, Variable value) {
        flow(()-> {
            fieldSteps.checkFieldAttribute(attribute.getValue(), fieldName.getValue(), CompareOperatorEnum.CONTAINS, value.getValue());
            /*
            String currentAttribute = fieldSteps.getFieldAttribute(fieldName.getValue(), attribute.getValue());
            if (currentAttribute == null)
                currentAttribute = "";
            Assert.assertTrue(String.format("Значение атрибута [%s] поля [%s] не содержит [%s]", attribute.getValue(), fieldName.getValue(), value.getValue()), currentAttribute.contains(value.getValue()));
            */
        });
    }


    @Then("выполнено нажатие на \"$fieldName\" правой кнопкой мыши$")
    public void stepClickRightField(Variable fieldName) {
        flow(() -> {
            if (fieldName.getValue().isEmpty()) {
                return;
            }
            fieldSteps.rightClickField(fieldName.getValue());
        });
    }

    @Then("выполнено наведение мыши на \"$fieldName\"")
    public void stepMoveMouseToField(Variable fieldName) {
        flow(() -> {
            if (fieldName.getValue().isEmpty()) {
                return;
            }
            fieldSteps.moveMouseToField(fieldName.getValue());
        });
    }

    @Then("выполнена прокрутка на \"$fieldName\"")
    public void stepScrollIntoViewField(Variable fieldName) {
        flow(() -> {
            if (fieldName.getValue().isEmpty()) {
                return;
            }
            fieldSteps.scrollIntoViewField(fieldName.getValue());
        });
    }

    /*
    @Then("в поле \"$fieldName\" ошибка \"$errorMsg\"")
    public void stepCheckFieldError(String fieldName, String errorMsg) {
        flow(()->
                fieldSteps.checkFieldError(fieldName, (String) evalVariable(errorMsg))
        );
    }

    @Then("лейбл поля \"$fieldName\" равен \"$errorMsg\"")
    public void stepCheckFieldLabel(String fieldName, String errorMsg) {
        flow(()-> {
            String actualError = AbstractPageObject.getCurrentPage().getField(fieldName).getLabel();
            assertEquals(String.format("Лейбл в поле [%s] не соответствует ожиданиям.", fieldName), errorMsg, actualError);
        });
    }

    @When("ввод значения \"$value\" через калькулятор")
    public void setValueByCalculator(String value){
        flow(()-> {
            String val = evalVariable(value);
            char[] chars = val.toCharArray();
            fieldSteps.setValueByCalculator(chars);
        });
    }

    @When("ожидается исчезновение окна загрузки")
    public void stepwaitDownloadWindowInVisible(){
        flow(()->
                fieldSteps.waitDownloadWindowInVisible()
        );
    }

    @When("при появлении окна \"$page\" происходит нажатие на кнопку \"$buttonName\"")
    public void stepClickIfDisplayedPage(Page page, String buttonName) {
        flow(()->
                fieldSteps.clickIfDisplayedPage(context, buttonName)
        );
    }

    @When("указатель мыши смещается к полю \"$fieldName\"")
    public void moveToElement(String fieldName){
        flow(()->
                fieldSteps.moveToElement(fieldName)
        );
    }

    @Then("поле \"$fieldName\" выбрано")
    public void stepFieldIsSelected(String fieldName){
        flow(()->
                fieldSteps.fieldIsSelected(fieldName)
        );
    }

    @Then("курсор находится в поле \"$fieldName\"")
    public void stepFieldHasKeyboardFocus(String fieldName){
        flow(()->
                fieldSteps.fieldHasKeyboardFocus(fieldName);
        );
    }

    @When("в списке \"$fieldName\" выбран \"$index\" элемент")
    public void selectItemByIndex(String fieldName, String index) {
        flow(()->
                fieldSteps.selectItemByIndex(evalVariable(fieldName),Integer.parseInt(evalVariable(index)));
        );
    }

    @When("ожидается исчезновение окна загрузки поля \"$fieldName\" в течение \"$seconds\" {секунд|секунды}")
    public void stepWaitDownloadFieldInVisible(String fieldName,int sec){
        flow(()->
                fieldSteps.waitDownloadFieldInVisible(fieldName,sec);
        );
    }
     */

}
