package ru.ibsqa.qualit.steps;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.compare.ICompareManager;
import ru.ibsqa.qualit.context.*;
import ru.ibsqa.qualit.elements.*;
import ru.ibsqa.qualit.utils.spring.SpringUtils;

import java.text.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

@Component
@Slf4j
public class CollectionSteps extends AbstractSteps {

    @Autowired
    private IContextExplorer contextExplorer;

    @Autowired
    private IFieldNameResolver fieldNameResolver;

    @Autowired
    private ICompareManager compareManager;

    @Data
    @Builder
    public static class FindCondition {
        private String fieldName;
        private String operator;
        private String value;

        public String getOperator() {
            return Optional.ofNullable(operator)
                    .orElseGet(() -> SpringUtils.getBean(ICompareManager.class).defaultOperator());
        }
    }

    @UIStep
    @TestStep("выбирается элемент коллекции \"${collectionName}\" с параметрами: \"${conditions}\"")
    @SuppressWarnings("rawtypes")
    public void stepSetCollectionByConditions(String collectionName, List<FindCondition> conditions) {
        PickElementResult<IFacadeCollection, ?> pickElementResult = getCollection(collectionName);
        pickElementResult.getContextManager().setContextCollectionItem(searchItem(collectionName, conditions));
    }

    @UIStep
    @TestStep("выбирается элемент коллекции \"${collectionName}\" с индексом \"${index}\"")
    @SuppressWarnings("rawtypes")
    public void stepSetCollectionByIndex(String collectionName, int index) {
        PickElementResult<IFacadeCollection, ?> pickElementResult = getCollection(collectionName);
        pickElementResult.getContextManager().setContextCollectionItem(searchItemByIndex(collectionName, index));
    }

    public void stepCheckItemCount(String collectionName, int count) {
        stepCheckItemCount(collectionName, compareManager.defaultOperator(), count);
    }

    @UIStep
    @TestStep("проверяется коллекция \"${collectionName}\", что количество элементов ${operator} \"${count}\"")
    public void stepCheckItemCount(String collectionName, String operator, int count) {
        IFacadeCollection<?> collection = getCollection(collectionName).getElement();
        AtomicReference<String> actual = new AtomicReference<>();
        boolean isChecked;
        if (collection instanceof IFacadeWait) {
            isChecked = waiting(
                    Duration.ofSeconds(((IFacadeWait) collection).getWaitTimeOut()),
                    () -> {
                        actual.set(Integer.toString(getItems(collection, collectionName).size()));
                        return compareManager.checkValue(operator, actual.get(), Integer.toString(count));
                    }
            );
        } else {
            actual.set(Integer.toString(getItems(collection, collectionName).size()));
            isChecked = compareManager.checkValue(operator, actual.get(), Integer.toString(count));
        }
        if (!isChecked) {
            fail(compareManager.buildErrorMessage(operator, message("checkCollection"), actual.get(), Integer.toString(count)));
        }
    }

    @UIStep
    @TestStep("проверяется, что коллекция \"${collectionName}\" не пуста")
    public void stepCheckNotEmpty(String collectionName) {
        stepCheckItemCount(collectionName, "больше", 0);
    }

    public void stepCheckItemCount(String collectionName, int count, List<FindCondition> conditions) {
        stepCheckItemCount(collectionName, compareManager.defaultOperator(), count);
    }

    @UIStep
    @TestStep("проверяется коллекция \"${collectionName}\", что количество элементов ${operator} \"${count}\" с параметрами: \"${conditions}\"")
    public void stepCheckItemCount(String collectionName, String operator, int count, List<FindCondition> conditions) {
        IFacadeCollection<?> collection = getCollection(collectionName).getElement();
        AtomicReference<String> actual = new AtomicReference<>();
        boolean isChecked;
        if (collection instanceof IFacadeWait) {
            isChecked = waiting(
                    Duration.ofSeconds(((IFacadeWait) collection).getWaitTimeOut()),
                    () -> {
                        try {
                            actual.set(Integer.toString(getItems(collection, collectionName, conditions, false).size()));
                            return compareManager.checkValue(operator, actual.get(), Integer.toString(count));
                        } catch (InvokeFieldException ignore) {
                            return false;
                        }
                    }
            );
        } else {
            actual.set(Integer.toString(getItems(collection, collectionName, conditions, false).size()));
            isChecked = compareManager.checkValue(operator, actual.get(), Integer.toString(count));
        }
        if (!isChecked) {
            fail(compareManager.buildErrorMessage(operator, message("checkCollection"), actual.get(), Integer.toString(count)));
        }
    }

    @UIStep
    @TestStep("получить элемент коллекции \"${collectionName}\" с индексом \"${index}\"")
    @SuppressWarnings("unchecked")
    public <CONTEXT extends IContextObject> CONTEXT searchItemByIndex(String collectionName, int index) {
        IFacadeCollection<CONTEXT> collection = getCollection(collectionName).getElement();

        long startTime = System.currentTimeMillis();
        do {
            Iterator<CONTEXT> iter = collection.iterator();
            int i = 0;
            while (iter.hasNext()) {
                CONTEXT item = iter.next();
                if (i == index) {
                    return item;
                }
                i++;
            }
        } while (collection instanceof IFacadeWait && (System.currentTimeMillis() < (startTime + ((IFacadeWait) collection).getWaitTimeOut() * 1000)));

        fail(message("collectionNoItemByIndexErrorMessage", collectionName, index));
        return null;
    }

    @UIStep
    @TestStep("получить элемент коллекции \"${collectionName}\" с параметрами: \"${conditions}\"")
    @SuppressWarnings("unchecked")
    public <CONTEXT extends IContextObject> CONTEXT searchItem(String collectionName, List<FindCondition> conditions) {
        IFacadeCollection<CONTEXT> collection = getCollection(collectionName).getElement();

        getStepListenerManager().setIgnoredMode(true);
        try {
            long startTime = System.currentTimeMillis();
            do {
                try {
                    List<CONTEXT> found = getItems(collectionName, conditions, true);
                    if (found.size() > 0) {
                        return found.get(0);
                    }
                } catch (InvokeFieldException ignore) {
                }
            } while (collection instanceof IFacadeWait && (System.currentTimeMillis() < (startTime + ((IFacadeWait) collection).getWaitTimeOut() * 1000)));

            fail(message("CollectionItemNotFoundAssertMessage", collectionName, conditions.toString()));
            return null;
        } finally {
            getStepListenerManager().setIgnoredMode(false);
        }
    }

    @UIStep
    @TestStep("ожидается элемент коллекции \"${collectionName}\" в течение \"${sec}\" сек с параметрами: \"${conditions}\"")
    @SuppressWarnings("rawtypes")
    public void waitCollectionByConditions(String collectionName, int sec, List<FindCondition> conditions) {
        PickElementResult<IFacadeCollection, ?> pickElementResult = this.getCollection(collectionName);
        IContextObject contextObject = waitObjectInCollectionByConditions(collectionName, sec, conditions);
        if (Objects.isNull(contextObject)) {
            fail(message("CollectionItemNotFoundAssertMessage", collectionName, conditions.toString()));
        } else {
            pickElementResult.getContextManager().setContextCollectionItem(contextObject);
        }
    }

    public IContextObject waitObjectInCollectionByConditions(String collectionName, int sec, List<FindCondition> conditions) {
        AtomicReference<IContextObject> contextObject = new AtomicReference<>();
        if (waiting(Duration.ofSeconds(sec), () -> {
            try {
                List<IContextObject> contextObjects = getItems(collectionName, conditions);
                if (contextObjects.size() > 0) {
                    contextObject.set(contextObjects.get(0));
                    return true;
                } else {
                    return false;
                }
            } catch (InvokeFieldException ignore) {
                return false;
            }
        })) {
            return contextObject.get();
        } else {
            return null;
        }
    }

    @UIStep
    @TestStep("ожидается элемент коллекции \"${collectionName}\" в течение \"${sec}\"")
    public void waitCollectionElements(String collectionName, int sec) {
        if (!waiting(Duration.ofSeconds(sec), () -> {
            try {
                return getItems(collectionName).size() > 0;
            } catch (AssertionError ae) {
                return false;
            }
        })) {
            fail(message("CollectionItemsNotFoundAssertMessage", collectionName));
        }
    }

    /**
     * Поиск коллекции по имени
     *
     * @param collectionName - имя коллекции
     * @param <CONTEXT>
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <CONTEXT extends IContextObject> PickElementResult<IFacadeCollection, CONTEXT> getCollection(String collectionName) {
        return contextExplorer.pickElement(collectionName, IFacadeCollection.class);
    }

    /**
     * Получение всех элементов коллекции
     *
     * @param collectionName - имя коллекции
     * @param <CONTEXT>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <CONTEXT extends IContextObject> List<CONTEXT> getItems(String collectionName) {
        return getItems(getCollection(collectionName).getElement(), collectionName);
    }

    /**
     * Получение всех элементов коллекции
     *
     * @param collection - коллекция
     * @param <CONTEXT>
     * @return
     */
    @UIStep
    @TestStep("получить элементы коллекции \"${collectionName}\"")
    public <CONTEXT extends IContextObject> List<CONTEXT> getItems(IFacadeCollection<CONTEXT> collection, String collectionName) {
        List<CONTEXT> result = new ArrayList<>();

        collection.forEach(result::add);

        return result;
    }

    /**
     * Получение элементов коллекции с параметрами
     *
     * @param collectionName - имя коллекции
     * @param conditions     - условия поиска
     * @param <CONTEXT>
     * @return
     */
    public <CONTEXT extends IContextObject> List<CONTEXT> getItems(String collectionName, List<FindCondition> conditions) {
        return getItems(collectionName, conditions, false);
    }

    /**
     * Получение элементов коллекции с параметрами
     *
     * @param collectionName - имя коллекции
     * @param conditions     - условия поиска
     * @param onlyFirst      - если true, то прекратить поиск после обнаружения первого элемента
     * @param <CONTEXT>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <CONTEXT extends IContextObject> List<CONTEXT> getItems(String collectionName, List<FindCondition> conditions, boolean onlyFirst) {
        return getItems(getCollection(collectionName).getElement(), collectionName, conditions, onlyFirst);
    }

    /**
     * Получение элементов коллекции с параметрами
     *
     * @param collection - коллекция
     * @param conditions - условия поиска
     * @param <CONTEXT>
     * @return
     */
    public <CONTEXT extends IContextObject> List<CONTEXT> getItems(IFacadeCollection<CONTEXT> collection, String collectionName, List<FindCondition> conditions) {
        return getItems(collection, collectionName, conditions, false);
    }

    /**
     * Получение элементов коллекции с параметрами
     *
     * @param collection - коллекция
     * @param conditions - условия поиска
     * @param onlyFirst  - если true, то прекратить поиск после обнаружения первого элемента
     * @param <CONTEXT>
     * @return
     */
    @UIStep
    @TestStep("получить элементы коллекции \"${collectionName}\" с параметрами \"${conditions}\"")
    public <CONTEXT extends IContextObject> List<CONTEXT> getItems(IFacadeCollection<CONTEXT> collection, String collectionName, List<FindCondition> conditions, boolean onlyFirst) {
        List<CONTEXT> result = new ArrayList<>();

        for (CONTEXT item : collection) {
            boolean match = true;

            StringBuilder logText = new StringBuilder(String.format("Получен элемент коллекции \"%s\" с параметрами:\n", collectionName));

            for (FindCondition row : conditions) {
                // Отделить имя поля и параметры
                String fieldName = null;
                try {
                    fieldName = fieldNameResolver.resolveParams(row.getFieldName());
                } catch (SearchElementException e) {
                    fail(e.getMessage());
                }

                IFacade field = item.getField(fieldName);
                String expected = evalVariable(row.getValue()).replace("\\n", "\n");
                if (null == field) {
                    fail(message("fieldNotExistsAssertMessage", row.getFieldName()));
                }
                if (field instanceof IFacadeReadable) {
                    IFacadeReadable readableField = (IFacadeReadable) field;
                    String actual = readableField.getFieldValue();
                    if (actual == null) {
                        actual = "";
                    } else {
                        actual = actual.trim();
                    }

                    logText.append(String.format("%s: %s\n", fieldName, actual));
                    match = compareManager.checkValue(row.getOperator(), actual, expected);
                }

                if (!match) {
                    logText.append(String.format("Элемент коллекции \"%s\" не подошел по параметру: \"%s\". Ожидалось: \"%s\" %s \"%s\"",
                            collectionName, fieldName, fieldName, row.getOperator(), expected));

                    log.debug(logText.toString());
                    break;
                }
            }

            if (match) {
                result.add(item);
                log.debug(String.format("%sЭлемент коллекции \"%s\" подошел по всем заданным параметрам поиска.", logText, collectionName));
                if (onlyFirst) {
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Извлечение значений из строки коллекции
     *
     * @param item
     * @param sortParamsMap
     * @return
     */
    public Map<String, String> getRow(IContextObject item, Map<String, SortParams> sortParamsMap) {
        Map<String, String> row = new HashMap<>();
        sortParamsMap.keySet().forEach(
                field -> {
                    IFacadeReadable elItem = item.getField(field);
                    row.put(field, elItem.getFieldValue());
                }
        );
        return row;
    }

    private static String getCollectionSortError(String message) {
        return message("collectionSortErrorPrefix", message);
    }

    /**
     * Класс, описывающий условия проверки сортировки по одному полю
     */
    @Data
    @SuppressWarnings("rawtypes")
    public static class SortParams {

        private Class clazz = null;
        private String pattern = null;
        private Character decimalSeparator = null;
        private Character groupingSeparator = null;
        private Boolean desc = false;
        private Boolean ignoreCase = false;

        public void check() {
            if (null == clazz) {
                fail(getCollectionSortError(message("collectionSortParamTypeWrong")));
            }

            if (null == pattern && (Date.class == clazz || Double.class == clazz || Long.class == clazz)) {
                fail(getCollectionSortError(message("collectionSortParamTemplateWrong")));
            }
        }

        private DateFormat dateFormat = null;

        private DateFormat getDateFormat() {
            if (null == dateFormat) {
                dateFormat = new SimpleDateFormat(null == pattern ? "dd.MM.yyyy" : pattern);
            }
            return dateFormat;
        }

        private DecimalFormat decimalFormat = null;

        private DecimalFormat getDecimalFormat() {
            if (null == decimalFormat) {
                decimalFormat = new DecimalFormat(null == pattern ? "#0.#" : pattern);
                DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
                decimalFormatSymbols.setDecimalSeparator(null == decimalSeparator ? ',' : decimalSeparator);
                decimalFormatSymbols.setGroupingSeparator(null == groupingSeparator ? ' ' : groupingSeparator);
            }
            return decimalFormat;
        }

        private Date getDate(String s) {
            if (null == s || s.isEmpty()) {
                return null;
            }
            try {
                return getDateFormat().parse(s);
            } catch (ParseException e) {
                fail(getCollectionSortError(message("collectionSortParamTypeDateWrong", s)));
                return null;
            }
        }

        private Double getDouble(String s) {
            if (null == s || s.isEmpty()) {
                return null;
            }
            Double d = null;
            try {
                return getDecimalFormat().parse(s).doubleValue();
            } catch (ParseException e) {
                fail(getCollectionSortError(message("collectionSortParamTypeDecimalWrong", s)));
                return null;
            }
        }

        private Long getLong(String s) {
            if (null == s || s.isEmpty()) {
                return null;
            }
            try {
                return getDecimalFormat().parse(s).longValue();
            } catch (ParseException e) {
                fail(getCollectionSortError(message("collectionSortParamTypeIntegerWrong", s)));
                return null;
            }
        }

        private String getString(String s) {
            if (null != s) {
                return ignoreCase ? s.toUpperCase() : s;
            }
            return null;
        }

        /**
         * Сравнение двух значений с учетом параметров
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        public int compare(String s1, String s2) {

            Comparable c1 = null;
            Comparable c2 = null;

            if (Date.class == clazz) { // Даты
                c1 = getDate(s1);
                c2 = getDate(s2);
            } else if (Double.class == clazz) { // Вещественные числа
                c1 = getDouble(s1);
                c2 = getDouble(s2);
            } else if (Long.class == clazz) { // Целые числа
                c1 = getLong(s1);
                c2 = getLong(s2);
            } else { // Строки
                c1 = getString(s1);
                c2 = getString(s2);
            }

            if (c1 == null && c2 == null) {
                return 0;
            } else if (c1 == null) {
                return -1;
            } else if (c2 == null) {
                return 1;
            } else {
                return (desc ? -1 : 1) * c1.compareTo(c2);
            }
        }
    }

    /**
     * Проверка сортировки коллекции
     *
     * @param array         - массив значений
     * @param sortParamsMap - параметры проверки сортировки
     * @return true, если коллекция отсортирована
     */
    private boolean checkSortArray(List<Map<String, String>> array, Map<String, SortParams> sortParamsMap) {
        try {
            array.stream().reduce((last, curr) -> {
                if (compare(last, curr, sortParamsMap) > 0) {
                    throw new UnsupportedOperationException();
                }
                return curr;
            });
            return true;
        } catch (UnsupportedOperationException ex) {
            return false;
        }
    }

    /**
     * Проверка сортировки коллекции
     *
     * @param collectionName имя коллекции для вывода сообщения
     * @param array          - массив значений
     * @param sortParamsMap  - параметры проверки сортировки
     */
    public void checkSortArray(String collectionName, List<Map<String, String>> array, Map<String, SortParams> sortParamsMap) {
        if (!checkSortArray(array, sortParamsMap)) {
            fail(message("collectionNotSorted", collectionName));
        }
    }

    /**
     * Сравнение порядка двух элементов
     *
     * @param m1            the first object to be compared
     * @param m2            the second object to be compared
     * @param sortParamsMap параметры сравнения
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second
     */
    private int compare(Map<String, String> m1, Map<String, String> m2, Map<String, SortParams> sortParamsMap) {
        return sortParamsMap.keySet().stream()
                .map(k -> sortParamsMap.get(k).compare(m1.get(k), m2.get(k)))
                .filter(i -> i != 0)
                .findFirst().orElse(0);
    }

    /**
     * Общая функция проверки сортировки
     *
     * @param collectionName - имя коллекции
     * @param conditions     - условия
     */
    @SuppressWarnings("unchecked")
    private void checkSortedInternal(String collectionName, List<Pair<String, String>> conditions) {

        Map<String, SortParams> sortParamsMap = new HashMap<>();
        SortParams sortParams = null;
        AtomicReference<String> groupField = new AtomicReference<>();

        for (Pair<String, String> entry : conditions) {

            String conditionField = entry.getKey();
            String conditionValue = entry.getValue();

            if (conditionField.equalsIgnoreCase(message("collectionSortParamField"))) {
                if (null != sortParams) {
                    sortParams.check();
                }
                sortParams = new SortParams();
                sortParamsMap.put(conditionValue, sortParams);
            } else {
                if (null == sortParams) {
                    fail(message("collectionSortParamFieldWrong"));
                }
                if (conditionField.equalsIgnoreCase(message("collectionSortParamType"))) {
                    if (conditionValue.equalsIgnoreCase(message("collectionSortParamTypeString"))) {
                        sortParams.setClazz(String.class);
                    } else if (conditionValue.equalsIgnoreCase(message("collectionSortParamTypeInteger"))) {
                        sortParams.setClazz(Long.class);
                    } else if (conditionValue.equalsIgnoreCase(message("collectionSortParamTypeDecimal"))) {
                        sortParams.setClazz(Double.class);
                    } else if (conditionValue.equalsIgnoreCase(message("collectionSortParamTypeDate"))) {
                        sortParams.setClazz(Date.class);
                    }
                } else if (conditionField.equalsIgnoreCase(message("collectionSortParamTemplate"))) {
                    sortParams.setPattern(conditionValue);
                } else if (conditionField.equalsIgnoreCase(message("collectionSortParamDecimalDelimeter"))) {
                    sortParams.setDecimalSeparator(conditionValue.charAt(0));
                } else if (conditionField.equalsIgnoreCase(message("collectionSortParamGroupDelimeter"))) {
                    sortParams.setGroupingSeparator(conditionValue.charAt(0));
                } else if (conditionField.equalsIgnoreCase(message("collectionSortParamOrder"))) {
                    if (conditionValue.equalsIgnoreCase(message("collectionSortParamOrderDesc"))) {
                        sortParams.setDesc(true);
                    }
                } else if (conditionField.equalsIgnoreCase(message("collectionSortParamOrderIgnoreCase"))) {
                    sortParams.setIgnoreCase(Boolean.parseBoolean(conditionValue));
                } else if (conditionField.equalsIgnoreCase(message("collectionSortParamGroupField"))) {
                    groupField.set(conditionValue);
                }
            }

        }
        if (null != sortParams) {
            sortParams.check();
        }

        final IFacadeCollection<IContextObject> collection = getCollection(collectionName).getElement();

        boolean isChecked;
        if (collection instanceof IFacadeWait) {
            isChecked = waiting(
                    Duration.ofSeconds(((IFacadeWait) collection).getWaitTimeOut()),
                    () -> waitForSortedInternal(sortParamsMap, groupField.get(), this.getItems(collection, collectionName))
            );
        } else {
            isChecked = waitForSortedInternal(sortParamsMap, groupField.get(), this.getItems(collection, collectionName));
        }

        if (!isChecked) {
            fail(message("collectionNotSorted", collectionName));
        }
    }

    private boolean waitForSortedInternal(Map<String, SortParams> sortParamsMap, String groupField, List<IContextObject> items) {
        try {
            if (null == groupField) {
                // Получить значения из коллекции
                List<Map<String, String>> array = items.stream()
                        .map(item -> getRow(item, sortParamsMap))
                        .collect(Collectors.toList());

                return checkSortArray(array, sortParamsMap);
            } else {
                // Разбить проверяемую коллекцию на группы
                Map<String, List<Map<String, String>>> groups = items.stream()
                        .collect(
                                Collectors.groupingBy(
                                        item -> {
                                            IFacadeReadable elItem = ((IContextObject) item).getField(groupField);
                                            return elItem.getFieldValue();
                                        },
                                        Collectors.mapping(
                                                item -> getRow(item, sortParamsMap), Collectors.toList()
                                        )
                                )
                        );

                // Проверить каждую группу на сортировку
                return groups.values().stream().allMatch(list ->
                        checkSortArray(list, sortParamsMap)
                );
            }
        } catch (InvokeFieldException ignore) {
            return false;
        }
    }


    @UIStep
    @TestStep("проверяется сортировка коллекции \"${collectionName}\", с параметрами: \"${conditions}\"")
    public void checkSorted(String collectionName, List<Pair<String, String>> conditions) {
        checkSortedInternal(collectionName, conditions);
    }

    @UIStep
    @TestStep("проверяется коллекция \"${collectionName}\", отсортирована ли по полю \"${field}\" с параметрами: \"${conditions}\"")
    public void checkSorted(String collectionName, String field, List<Pair<String, String>> conditions) {
        conditions.add(0, Pair.of(message("collectionSortParamField"), field));
        checkSortedInternal(collectionName, conditions);
    }

}
