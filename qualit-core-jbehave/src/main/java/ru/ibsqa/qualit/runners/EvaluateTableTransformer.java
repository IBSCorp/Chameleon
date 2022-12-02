package ru.ibsqa.qualit.runners;

import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.TableParsers;
import org.jbehave.core.model.TableTransformers;

public class EvaluateTableTransformer implements TableTransformers.TableTransformer {

    IEvaluateManager evaluateManager = SpringUtils.getBean(IEvaluateManager.class);

    private static final String ROW_SEPARATOR = "\n";
    private static final String ROW_SEPARATOR2 = "\r";

    public EvaluateTableTransformer() {
    }

    private boolean ignoreRow(String rowAsString, String ignorableSeparator) {
        return rowAsString.startsWith(ignorableSeparator) || rowAsString.length() == 0;
    }

    @Override
    public String transform(String s, TableParsers tableParsers, ExamplesTable.TableProperties tableProperties) {
        String ignorableSeparator = tableProperties.getIgnorableSeparator();

        String[] rows = s.split(ROW_SEPARATOR);
        int length = rows.length;

        StringBuilder builder = new StringBuilder();
        int r;
        String rowAsString;
        for(r = 0; r < length; ++r) {
            rowAsString = evaluateManager.evalVariable(rows[r]);
            rowAsString.replaceAll(ROW_SEPARATOR2,"");
            if (!this.ignoreRow(rowAsString, ignorableSeparator)) {
                builder.append(rowAsString).append(ROW_SEPARATOR);
            }
        }

        return builder.toString();
    }
}
