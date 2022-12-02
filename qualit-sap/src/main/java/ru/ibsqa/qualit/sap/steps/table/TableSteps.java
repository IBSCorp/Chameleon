package ru.ibsqa.qualit.sap.steps.table;

import ru.ibsqa.qualit.sap.elements.SapTable;
import ru.ibsqa.qualit.sap.elements.SapTableControl;
import ru.ibsqa.qualit.sap.elements.SapTreeTable;
import ru.ibsqa.qualit.steps.CoreFieldSteps;
import ru.ibsqa.qualit.steps.SeleniumFieldSteps;
import ru.ibsqa.qualit.steps.UIStep;
import ru.ibsqa.qualit.steps.TestStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableSteps extends CoreFieldSteps {

    @Autowired
    private SeleniumFieldSteps seleniumFieldSteps;


    @UIStep
    @TestStep("открыто меню \"${field}\" по пути \"${path}\"")
    public void stepSelectMenuByPath(String field, String path) {
        SapTreeTable table = seleniumFieldSteps.getSeleniumField(field);
        table.selectByPath(path);
    }

    @UIStep
    @TestStep("установлено значение \"${value}\" в таблице \"${field}\" колонка \"${column}\" строка \"${row}\"")
    public void stepSetCellValueByRowNameIndex(String field, String column, String row, String value) {
        SapTable sapTable = seleniumFieldSteps.getSeleniumField(field);
        sapTable.setCellValue(column, row, value);
    }

    @UIStep
    @TestStep("выполнено нажатие кнопки \"${buttonName}\" в таблице \"${field}\"")
    public void stepClickButtonInTable(String field, String buttonName) {
        SapTable sapTable = seleniumFieldSteps.getSeleniumField(field);
        sapTable.clickButton(buttonName);
    }

    @UIStep
    @TestStep("получено значение из таблицы \"${field}\" колонка \"${title}\" строка \"${index}\"")
    public String stepGetCellValueByColumnIndex(String field, String title, int index) {
        SapTable sapTable = seleniumFieldSteps.getSeleniumField(field);
        return sapTable.getCellValueByRowName(title, index);
    }

    @UIStep
    @TestStep("в таблице \"${field}\" выполнен двойной клик в строке \"${index}\"")
    public void stepClickInTableByIndex(String field, int index) {
        SapTableControl sapTable = seleniumFieldSteps.getSeleniumField(field);
        sapTable.selectRowByIndex(index);
    }

    @TestStep("в таблице \"${field}\" присутствует колонка \"${columnName}\" со значением \"${value}\"")
    public void stepSelectColumnValue(String fieldName, String columnName, String value) {
        SapTableControl sapTable = seleniumFieldSteps.getSeleniumField(fieldName);
        sapTable.rowExist(columnName, value);
    }
}
