package ru.ibsqa.qualit.sap.steps.table;

import io.cucumber.java.ru.Когда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.converters.FieldValueTable;
import ru.ibsqa.qualit.steps.AbstractSteps;
import ru.ibsqa.qualit.steps.ICollectionUtils;
import ru.ibsqa.qualit.steps.StepDescription;

import java.util.List;

public class GridStorySteps extends AbstractSteps implements ICollectionUtils {

    @Autowired
    private GridSteps gridSteps;

    @StepDescription(
            action = "SAP Automation->Нажатие на кнопку в гриде",
            parameters = {"filed - наименование грида", "buttonName - наименование кнопки"}
    )
    @Когда("^в гриде \"(.*)\" выполнено нажатие на кнопку \"(.+)\"$")
    public void stepClickTableButton(String field, String buttonName) {
        flow(() -> {
            gridSteps.stepClickButtonInTable(evalVariable(field), buttonName);
        });
    }

    @StepDescription(
            action = "SAP Automation->Выбор контекстного в гриде",
            parameters = {"filed - наименование грида", "menu - пункт контекстного меню"}
    )
    @Когда("^в гриде \"(.*)\" выбран пункт контекстного меню \"(.+)\"$")
    public void stepSelectContextMenu(String field, String menuName) {
        flow(() -> {
            gridSteps.stepSelectContextMenu(evalVariable(field), menuName);
        });
    }

    @StepDescription(
            action = "SAP Automation->Присутствует грид с параметрами",
            parameters = {"filed - наименование грида", "conditions - параметры элемента"}
    )
    @Когда("^присутствует элемент грида \"(.*)\" с параметрами:$")
    public void stepExistGridWithParams(String field, List<FieldValueTable> conditions) {
        flow(() -> {
            gridSteps.stepExistGridWithParams(evalVariable(field), parsePairs(conditions, getEvaluateManager()));
        });
    }

    @StepDescription(
            action = "SAP Automation->Выбор контекстного в гриде",
            parameters = {"filed - наименование грида", "buttonName -  наименование кнопки", "menu - пункт контекстного меню"}
    )
    @Когда("^в гриде \"(.*)\" выполнено нажатие на \"(.*)\" и выбран пункт контекстного меню \"(.+)\"$")
    public void stepClickButtonAndSelectContextMenu(String field, String buttonName, String menuName) {
        flow(() -> {
            gridSteps.stepClickContextButton(field, buttonName);
            gridSteps.stepSelectContextMenu(evalVariable(field), menuName);
        });
    }

    @StepDescription(
            action = "SAP Automation->Нажатие на ячейку в гриде",
            parameters = {"field - наименование грида", "column - наименование столбца", "row - индекс строки"}
    )
    @Когда("^в гриде \"(.*)\" выполнено нажатие на ячейку в ряду \"(.*)\" и столбце \"(.*)\"$")
    public void stepClickCellInTable(String field, String rowIndex, String columnName) {
        flow(() -> {
            gridSteps.getStepClickCellByRowIndexColumnName(field, columnName, rowIndex);
        });
    }

    @StepDescription(
            action = "SAP Automation->Сохранение значение из таблицы в в переменную",
            parameters = {"var - имя переменной", "fieldName - наименование таблицы", "rowIndex - индекс строки", "column - заголовок столбца"}
    )
    @Когда("^в переменной \"(.+)\" сохранено значение грида \"(.+)\" строка с индексом \"(.+)\" столбец с наименованием \"(.+)\"$")
    public void stepSaveGridVariable(String var, String fieldName, String rowIndex, String columnTitle) {
        setVariable(var, gridSteps.stepGetCellValueByColumnIndex(fieldName, rowIndex, columnTitle));
    }

    @StepDescription(
            action = "SAP Automation->Сохранение значение из таблицы в в переменную",
            parameters = {"var - имя переменной", "fieldName - наименование таблицы", "rowIndex - индекс строки", "column - индекс столбца"}
    )
    @Когда("^в переменной \"(.+)\" сохранено значение грида \"(.+)\" строка с индексом \"(.+)\" столбец с индексом \"(.+)\"$")
    public void stepSaveGridVariableByIndex(String var, String fieldName, String rowIndex, String columnIndex) {
        setVariable(var, gridSteps.stepGetCellValueByColumnAndRowIndex(fieldName, rowIndex, columnIndex));
    }

}
