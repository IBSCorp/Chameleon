package ru.ibsqa.chameleon.context;

import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.storage.IVariableScope;
import ru.ibsqa.chameleon.storage.IVariableStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private IFieldNameResolver fieldNameResolver;

    @Deprecated
    private List<IContextManager> contextManagers = new ArrayList<>();

    private List<IContextRegistrator> contextRegistrators;

    private ThreadLocal<IVariableScope> pickElementScope = new InheritableThreadLocal<>();

    private final Pattern contextPattern = Pattern.compile("(?<context>.*?)::(?<name>.*?)");

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

    @Deprecated
    @Override
    public void addContextManager(IContextManager contextManager) {
        contextManagers.add(contextManager);
    }

    @Deprecated
    @Override
    public void removeContextManager(IContextManager contextManager) {
        contextManagers.remove(contextManager);
    }

    @Override
    public synchronized IVariableScope resetPickElementScope() {
        pickElementScope.set(variableStorage.getDefaultScope().createChild());
        return pickElementScope.get();
    }

    @Override
    public synchronized IVariableScope getPickElementScope() {
        return (Objects.nonNull(pickElementScope.get())) ? pickElementScope.get() : variableStorage.getDefaultScope();
    }

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
