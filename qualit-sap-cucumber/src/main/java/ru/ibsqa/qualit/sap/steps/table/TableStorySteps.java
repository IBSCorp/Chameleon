package ru.ibsqa.qualit.sap.steps.table;

import io.cucumber.java.ru.Когда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.converters.FieldValueTable;
import ru.ibsqa.qualit.steps.AbstractSteps;
import ru.ibsqa.qualit.steps.StepDescription;

import java.util.List;

public class TableStorySteps extends AbstractSteps {

    @Autowired
    private TableSteps tableSteps;

    @StepDescription(
            action = "SAP Automation->Переход по дереву",
            parameters = {"field - наименование дерева", "path - путь перехода"}
    )
    @Когда("^в дереве \"(.+)\" выполнен переход по пути \"(.+)\"$")
    public void stepSelectMenuByPath(String field, String path) {
        flow(() ->
                tableSteps.stepSelectMenuByPath(evalVariable(field), evalVariable(path))
        );
    }

    @StepDescription(
            action = "SAP Automation->Переход по дереву",
            parameters = {"field - наименование дерева", "path - путь перехода"}
    )
    @Когда("^в дереве \"(.+)\" открыт пункт меню по пути \"(.+)\"$")
    public void stepDoubleClickMenuByPath(String field, String path) {
        flow(() ->
                tableSteps.stepSelectMenuByPath(evalVariable(field), evalVariable(path))
        );
    }

    @StepDescription(
            action = "SAP Automation->Заполнение поля в таблице",
            parameters = {"field - наименование таблицы", "column - наименование столбца", "row - наименование строки", "value - значение"}
    )
    @Когда("^в таблице \"(.*)\" заполнено поле столбец \"(.+)\" строка \"(.+)\" значением \"(.+)\"$")
    public void stepFillCellInTable(String field, String column, String row, String value) {
        flow(() -> {
            tableSteps.stepSetCellValueByRowNameIndex(evalVariable(field), evalVariable(column), evalVariable(row), evalVariable(value));
        });
    }

    @StepDescription(
            action = "SAP Automation->Нажатие на кнопку в таблице",
            parameters = {"field - наименование таблицы", "buttonName - наименование кнопки", }
    )
    @Когда("^в таблице \"(.*)\" выполнено нажатие на кнопку \"(.+)\"$")
    public void stepClickTableButton(String field, String buttonName) {
        flow(() -> {
            tableSteps.stepClickButtonInTable(evalVariable(field), buttonName);
        });
    }

    @StepDescription(
            action = "SAP Automation->Нажатие на кнопку в таблице",
            parameters = {"field - наименование таблицы", "buttonName - наименование кнопки", }
    )
    @Когда("^таблица \"(.*)\" в столбце \"(.*)\" заполняется следующими значениями:$")
    public void stepFillTable(String field, String column, List<FieldValueTable> fields) {
        this.flow(() -> {
            for (FieldValueTable fieldValue : fields) {
                tableSteps.stepSetCellValueByRowNameIndex(evalVariable(field), evalVariable(column),
                        evalVariable(fieldValue.getField()), evalVariable(fieldValue.getValue()));
            }
        });
    }

    @StepDescription(
            action = "SAP Automation->Сохранение значение из таблицы в в переменную",
            parameters = {"var - имя переменной", "fieldName - наименование таблицы", "rowTitle - заголовок строки", "column - индекс столбца"}
    )
    @Когда("^в переменной \"(.+)\" сохранено значение таблицы \"(.+)\" строка \"(.+)\" столбец \"(.+)\"$")
    public void stepSaveTableVariable(String var, String fieldName, String rowTitle, int column) {
        setVariable(var, tableSteps.stepGetCellValueByColumnIndex(fieldName, rowTitle, column));
    }

    @StepDescription(
            action = "SAP Automation->Выбор строки в таблице",
            parameters = {"fieldName - наименование таблицы",  "rowIndex - индекс строки"}
    )
    @Когда("^в таблице \"(.+)\" выбрана строке \"(.+)\"$")
    public void stepClickInTableByRowIndex(String fieldName, String rowIndex) {
        tableSteps.stepClickInTableByIndex(fieldName, Integer.parseInt(rowIndex));
    }

    @StepDescription(
            action = "SAP Automation->Присутствует таблица с параметрами",
            parameters = {"fieldName - наименование таблицы",
                    "columnName - заголовок колонки", "value - значение колонки"}
    )
    @Когда("^в таблице \"(.+)\" присутствует колонка \"(.+)\" со значением \"(.+)\"$")
    public void stepSelectColumnValue(String fieldName, String columnName, String value) {
        tableSteps.stepSelectColumnValue(fieldName, columnName, value);
    }

}
