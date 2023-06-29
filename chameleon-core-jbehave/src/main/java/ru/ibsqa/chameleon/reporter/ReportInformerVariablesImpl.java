package ru.ibsqa.chameleon.reporter;

import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.storage.IVariableStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReportInformerVariablesImpl implements IReportInformer {

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IVariableStorage variableStorage;

    @Override
    public String renderBefore() {
        return "";
    }

    @Override
    public String renderAfter() {
        Map<String, Object> parameters = variableStorage.getVariables(variableStorage.getRootScope());
        String header = "<div class=\"parameters__table_row\">" +
                "<div class=\"parameters__table_cell parameters__table_cell_name line-ellipsis\" style=\"font-weight: bold\">" +
                localeManager.getMessage("variableReportTitle")+"</div>" +
                "<div class=\"parameters__table_cell parameters__table_cell_value line-ellipsis\" style=\"font-weight: bold\">" +
                localeManager.getMessage("valueReportTitle")+"</div>" +
                "</div>" +
                "</div>";
        String rows = "";
        String rowTemplate = "<div class=\"parameters__table_row\">" +
                "<div class=\"parameters__table_cell parameters__table_cell_name line-ellipsis\">%s</div>" +
                "<div class=\"parameters__table_cell parameters__table_cell_value line-ellipsis\">%s</div>" +
                "</div>";
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            rows = rows + String.format(rowTemplate, parameter.getKey(), parameter.getValue());
        }
        String table = "<div class=\"parameters__table\">" +
                header + rows +
                "</div>";
        return table;
    }

}
