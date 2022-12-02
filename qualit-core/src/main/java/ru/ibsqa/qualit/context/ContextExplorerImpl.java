package ru.ibsqa.qualit.context;

import ru.ibsqa.qualit.elements.IFacade;
import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.storage.IVariableScope;
import ru.ibsqa.qualit.storage.IVariableStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ContextExplorerImpl implements IContextExplorer {

    @Autowired
    private IVariableStorage variableStorage;

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private IEvaluateManager evaluateManager;

    @Autowired
    private IFieldNameResolver fieldNameResolver;

    private List<IContextManager> contextManagers = new ArrayList<>();

    private List<IContextRegistrator> contextRegistrators;

    @Autowired
    private void collectContextRegistrators(List<IContextRegistrator> contextRegistrators) {
        this.contextRegistrators = contextRegistrators;
    }

    private List<IContextManager> getContextManagers() {
        List<IContextManager> allContextManagers = new ArrayList<>(contextManagers);
        if (null != contextRegistrators) {
            allContextManagers.addAll(
                    contextRegistrators
                        .stream()
                        .map(item -> item.getContextManager()).filter(item -> null!=item)
                        .collect(Collectors.toList())
            );
        }
        return allContextManagers;
    }

    @Override
    public void addContextManager(IContextManager contextManager) {
        contextManagers.add(contextManager);
    }

    @Override
    public void removeContextManager(IContextManager contextManager) {
        contextManagers.remove(contextManager);
    }

    private IVariableScope pickElementScope = null;

    @Override
    public IVariableScope resetPickElementScope() {
        pickElementScope = variableStorage.getDefaultScope().createChild();
        return pickElementScope;
    }

    @Override
    public IVariableScope getPickElementScope() {
        return (null != pickElementScope) ? pickElementScope : variableStorage.getDefaultScope();
    }

    private final Pattern paramsPattern = Pattern.compile("(?<name>.*?)\\{(?<variables>.*?)}");
    private final Pattern contextPattern = Pattern.compile("(?<context>.*?)::(?<name>.*?)");

    /**
     * Пример полного названия поля:
     *  имя_контекста::имя_поля{параметр1=>значение1,параметр2=>значение2,параметр3=>значение3}
     * если контекст не указан, происходит поиск во всех доступных контекстах
     * если параметры указаны, то значения передаются в локатор и могут быть там использованы через плейсхолдеры
     */
    // TODO переписать этот метод!
    @Override
    public <FACADE extends IFacade, CONTEXT extends IContextObject> PickElementResult<FACADE, CONTEXT> searchElement(String fullPathName, Class<FACADE> facadeClass) throws SearchElementException {

        String name = fullPathName.trim();

        // Отделить имя поля и параметры
        name = fieldNameResolver.resolveParams(name);
//        resetPickElementScope();
//        Matcher matcher = paramsPattern.matcher(name);
//        if (matcher.matches()) {
//            name = matcher.group("name").trim();
//            // Запятая разделитель параметров, то только если ей не предшествует символ \
//            List<String> variables = Arrays.asList(matcher.group("variables").split("(?<!\\\\),"));
//            for (String expression : variables) {
//                expression = expression.replace("\\,", ",");
//                String[] pair = expression.split("=>");
//                if (pair.length < 2) {
//                    throwSearchElementException(localeManager.checkValue("paramValueErrorMessage", pair[0], fullPathName));
//                }
//                pickElementScope.setVariable(pair[0].trim(),evaluateManager.evalVariable(pair[1].trim()));
//            }
//        }
        // Отделить имя контекста
        String context = null;
        Matcher matcher = contextPattern.matcher(name);
        if (matcher.matches()) {
            context = matcher.group("context").trim();
            name = matcher.group("name").trim();
        }

        List<String> allContexts = new ArrayList<>();
        List<String> findedContexts = new ArrayList<>();

        PickElementResult findedResult = null;
        for (IContextManager contextManager : getContextManagers()) {
            if (null == context || context.equals(contextManager.getContextCode())) {
                allContexts.add(contextManager.getContextName());
                PickElementResult result = contextManager.pickElement(name, facadeClass);
                if (null != result) {
                    findedContexts.add(contextManager.getContextName());
                    if (null != findedResult) {
                        throwSearchElementException(localeManager.getMessage("elementNotUniqueErrorMessage", name, String.join(",", findedContexts)));
                    }
                    findedResult = result;
                }
            }
        }

        if (null == findedResult) {
            if (null == context) {
                throwSearchElementException(localeManager.getMessage("elementNotFoundInContextsErrorMessage",
                        name, String.join(",", allContexts)));
            } else {
                if (allContexts.size() > 0) {
                    throwSearchElementException(localeManager.getMessage("elementNotFoundInContextErrorMessage", name, context));
                } else {
                    throwSearchElementException(localeManager.getMessage("contextNotFoundErrorMessage", context));
                }
            }
        }

        return findedResult;
    }

}
