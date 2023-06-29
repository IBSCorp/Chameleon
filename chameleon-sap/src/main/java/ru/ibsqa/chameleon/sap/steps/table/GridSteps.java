package ru.ibsqa.chameleon.sap.steps.table;

import org.apache.commons.lang3.tuple.Pair;
import ru.ibsqa.chameleon.sap.elements.SapGridView;
import ru.ibsqa.chameleon.steps.CoreFieldSteps;
import ru.ibsqa.chameleon.steps.SeleniumFieldSteps;
import ru.ibsqa.chameleon.steps.UIStep;
import ru.ibsqa.chameleon.steps.TestStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Component
public class GridSteps extends CoreFieldSteps {

    @Autowired
    private SeleniumFieldSteps seleniumFieldSteps;


    @UIStep
    @TestStep("в гриде \"${field}\" выполнено нажатие на кнопку \"${buttonName}\"")
    public void stepClickButtonInTable(String field, String buttonName) {
        SapGridView sapGridView = seleniumFieldSteps.getSeleniumField(field);
        sapGridView.clickButton(buttonName);
    }

    @UIStep
    @TestStep("в гриде \"${field}\" выбран пункт контекстного меню \"${menuName}\"")
    public void stepSelectContextMenu(String field, String menuName) {
        SapGridView sapGridView = seleniumFieldSteps.getSeleniumField(field);
        sapGridView.selectContextMenu(menuName);
    }

    @UIStep
    @TestStep("в гриде \"${field}\" нажата кнопка \"${contextButton}\" контекстного меню")
    public void stepClickContextButton(String field, String contextButton) {
        SapGridView sapGridView = seleniumFieldSteps.getSeleniumField(field);
        sapGridView.clickContextButton(contextButton);
    }


    @UIStep
    @TestStep("выбирается элемент грида \"${field}\" с параметрами: \"${conditions}\"")
    public void stepExistGridWithParams(String field, List<Pair<String,String>> conditions) {
        SapGridView sapGridView = seleniumFieldSteps.getSeleniumField(field);
        assertTrue(sapGridView.existRowWithParams(parseConditions(conditions)),
                String.format("Не найден элемент грида [%s] с параметрами [%s].", field, conditions.toString())
        );
    }


    @UIStep
    @TestStep("в гриде \"${field}\" выполнено нажатие на ячейку в ряду \"${rowIndex}\" и столбце \"${columnTitle}\"")
    public void getStepClickCellByRowIndexColumnName(String field, String columnTitle, String rowIndex) {
        SapGridView sapGridView = seleniumFieldSteps.getSeleniumField(field);
        sapGridView.clickCell(columnTitle, Integer.parseInt(rowIndex));
    }

    private Map<String, String> parseConditions(List<Pair<String,String>> conditions) {
        Map<String, String> map = new HashMap();
        Iterator var3 = conditions.iterator();

        while(var3.hasNext()) {
            Pair<String,String> fieldValue = (Pair<String,String>)var3.next();
            map.put(fieldValue.getKey(), this.evalVariable(fieldValue.getValue()));
        }

        return map;
    }

    @TestStep("получено значение грида \"${fieldName}\" с индексом строки \"${rowIndex}\" и заголовком столбца \"${columnTitle}\"")
    public String stepGetCellValueByColumnIndex(String fieldName, String rowIndex, String columnTitle) {
        SapGridView sapGridView = seleniumFieldSteps.getSeleniumField(fieldName);
        return sapGridView.getCellValue(columnTitle, Integer.parseInt(rowIndex));
    }

    @TestStep("получено значение грида \"${fieldName}\" с индексом строки \"${rowIndex}\" и индексом столбца \"${columnIndex}\"")
    public String stepGetCellValueByColumnAndRowIndex(String fieldName, String rowIndex, String columnIndex) {
        SapGridView sapGridView = seleniumFieldSteps.getSeleniumField(fieldName);
        return sapGridView.getCellValueByIndex(Integer.parseInt(columnIndex), Integer.parseInt(rowIndex));
    }
}
