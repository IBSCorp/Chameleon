package ru.ibsqa.qualit.utils;

import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import org.apache.commons.lang3.tuple.Pair;
import org.jbehave.core.model.ExamplesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExamplesTableParserImpl implements IExamplesTableParser {

    @Autowired
    private IEvaluateManager evaluateManager;

    /**
     * Преобразовать ExamplesTable в Map
     * @param examplesTable исходный набор данных
     * @param keyColumn название поля с ключом
     * @param valueColumn название поля со значением
     * @return
     */
    @Override
    public Map<String,String> parseToMap(ExamplesTable examplesTable, String keyColumn, String valueColumn) {

        Map<String,String> result = new HashMap<>();

        for (Map<String,String> row : examplesTable.getRows()) {
            result.put(row.get(keyColumn), evaluateManager.evalVariable(row.get(valueColumn)));
        }

        return result;
    }

    @Override
    public List<Pair<String, String>> parseToList(ExamplesTable examplesTable, String keyColumn, String valueColumn) {
        return examplesTable.getRows().stream()
                .map(row -> Pair.of(row.get(keyColumn), (String)evaluateManager.evalVariable(row.get(valueColumn))))
                .collect(Collectors.toList());
    }
}
