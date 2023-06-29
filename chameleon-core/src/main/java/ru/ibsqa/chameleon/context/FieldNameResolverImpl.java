package ru.ibsqa.chameleon.context;

import ru.ibsqa.chameleon.evaluate.IEvaluateManager;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.storage.IVariableScope;
import ru.ibsqa.chameleon.storage.IVariableStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FieldNameResolverImpl implements IFieldNameResolver, ISearchElementException {

    @Autowired
    private IVariableStorage variableStorage;

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IEvaluateManager evaluateManager;

    @Autowired
    private IContextExplorer contextExplorer;

    private final Pattern paramsPattern = Pattern.compile("(?<name>.*?)\\{(?<variables>.*?)}");

    @Override
    public String resolveParams(String fullPathName) throws SearchElementException {

        IVariableScope pickElementScope = contextExplorer.resetPickElementScope();

        String name = fullPathName;
        Matcher matcher = paramsPattern.matcher(name);
        if (matcher.matches()) {
            name = matcher.group("name").trim();
            // Запятая разделитель параметров, то только если ей не предшествует символ \
            List<String> variables = Arrays.asList(matcher.group("variables").split("(?<!\\\\),"));
            for (String variable : variables) {
                variable = variable.replace("\\,", ",");
                String[] pair = variable.split("=>");
                if (pair.length < 2) {
                    throwSearchElementException(localeManager.getMessage("paramValueErrorMessage", pair[0], fullPathName));
                }
                pickElementScope.setVariable(pair[0].trim(),evaluateManager.evalVariable(pair[1].trim()));
            }
        }

        return name;
    }
}
