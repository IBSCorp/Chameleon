package ru.ibsqa.qualit.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.jbehave.core.model.ExamplesTable;

import java.util.List;
import java.util.Map;

public interface IExamplesTableParser {
    Map<String,String> parseToMap(ExamplesTable examplesTable, String keyColumn, String valueColumn);
    List<Pair<String,String>> parseToList(ExamplesTable examplesTable, String keyColumn, String valueColumn);
}
