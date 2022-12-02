package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.context.*;
import ru.ibsqa.qualit.elements.IFacade;
import ru.ibsqa.qualit.elements.IFacadeCollection;
import ru.ibsqa.qualit.elements.IFacadeReadable;
import ru.ibsqa.qualit.utils.waiting.WaitingUtils;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Component
@Slf4j
// TODO i18n
public class CollectionSteps extends AbstractSteps {

    @Autowired
    private IContextExplorer contextExplorer;

    @Autowired
    private IFieldNameResolver fieldNameResolver;

    @Autowired
    private WaitingUtils waitingUtils;

    private static final String ERROR_MESSAGE_PREFIX = "При проверке сортировки произошла ошибка: ";

    @Data
    @Builder
    public static class FindCondition {
        private String fieldName;
        private CompareOperatorEnum operator;
        private String value;

        public CompareOperatorEnum getOperator() {
            return Optional.ofNullable(operator).orElse(CompareOperatorEnum.EQUALS);
        }
    }

    @UIStep
    @TestStep("выбирается элемент коллекции \"${collectionName}\" с параметрами: \"${conditions}\"")
    public void stepSetCollectionByConditions(String collectionName, List<FindCondition> conditions) {
        PickElementResult<IFacadeCollection, ?> pickElementResult = getCollection(collectionName);
        pickElementResult.getContextManager().setContextCollectionItem(searchItem(collectionName, conditions));
    }

    @UIStep
    @TestStep("выбирается элемент коллекции \"${collectionName}\" с индексом \"${index}\"")
    public void stepSetCollectionByIndex(String collectionName, int index) {
        PickElementResult<IFacadeCollection, ?> pickElementResult = getCollection(collectionName);
        pickElementResult.getContextManager().setContextCollectionItem(searchItemByIndex(collectionName, index));
    }

    @UIStep
    @TestStep("проверяется коллекция \"${collectionName}\", равно ли количество элементов \"${count}\"")
    public void stepCheckItemCount(String collectionName, int count) {
        int actualCount = getItems(collectionName).size();
        assertEquals (count, actualCount, "Количество элементов коллекции " + collectionName + " не соответствует ожидаемому");
    }


    @UIStep
    @TestStep("проверяется, что коллекция \"${collectionName}\" не пуста")
    public void stepCheckNotEmpty(String collectionName) {
        int actualCount = getItems(collectionName).size();
        assertNotEquals(0, actualCount, "Количество элементов коллекции " + collectionName + " равно нулю");
    }

    @UIStep
    @TestStep("проверяется коллекция \"${collectionName}\", равно ли количество элементов \"${count}\" с параметрами: \"${conditions}\"")
    public void stepCheckItemCount(String collectionName, int count, List<FindCondition> conditions) {
        int actualCount = getItems(collectionName, conditions, false).size();
        assertEquals(count, actualCount, "Количество элементов коллекции " + collectionName + " не соответствует ожидаемому");
    }

    @UIStep
    @TestStep("получить элемент коллекции \"${collectionName}\" с индексом \"${index}\"")
    public <CONTEXT extends IContextObject> CONTEXT searchItemByIndex(String collectionName, int index) {
        IFacadeCollection<CONTEXT> collection = getCollection(collectionName).getElement();

        Iterator<CONTEXT> iter = collection.iterator();
        int i = 0;
        while (iter.hasNext()) {
            CONTEXT item = iter.next();
            if (i == index) {
                return item;
            }
            i++;
        }

        fail(message("collectionNoItemByIndexErrorMessage", collectionName, index));
        return null;
    }

    @UIStep
    @TestStep("получить элемент коллекции \"${collectionName}\" с параметрами: \"${conditions}\"")
    public <CONTEXT extends IContextObject> CONTEXT searchItem(String collectionName, List<FindCondition> conditions) {
        List<CONTEXT> found = getItems(collectionName, conditions, true);
        if (0 == found.size()) {
            fail(message("CollectionItemNotFoundAssertMessage", collectionName, conditions.toString()));
        }
        return found.get(0);
    }

    @UIStep
    @TestStep("ожидается элемент коллекции \"${collectionName}\" в течение \"${sec}\" сек с параметрами: \"${conditions}\"")
    public void waitCollectionByConditions(String collectionName, int sec, List<FindCondition> conditions) {
        PickElementResult<IFacadeCollection, ?> pickElementResult = this.getCollection(collectionName);
        IContextObject contextObject = waitObjectInCollectionByConditions(collectionName, sec, conditions);
        if (Objects.isNull(contextObject)) {
            fail(this.message("CollectionItemNotFoundAssertMessage", new Object[]{collectionName, conditions.toString()}));
        } else {
            pickElementResult.getContextManager().setContextCollectionItem(contextObject);
        }
    }

    public IContextObject waitObjectInCollectionByConditions(String collectionName, int sec, List<FindCondition> conditions) {
        AtomicReference<IContextObject> contextObject = new AtomicReference<>();
        if (waitingUtils.waiting(sec * 1000, () -> {
            List<IContextObject> contextObjects = getItems(collectionName, conditions);
            if (contextObjects.size() > 0) {
                contextObject.set(contextObjects.get(0));
                return true;
            } else {
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
        if (!waitingUtils.waiting(sec * 1000, () -> {
            try {
                return getItems(collectionName).size() > 0;
            } catch (AssertionError ae) {
                return false;
            }
        })) {
            fail(String.format("Не появились элементы коллекции [%s] в течение [%s]", collectionName, sec));
        }
    }

    /**
     * Поиск коллекции по имени
     *
     * @param collectionName
     * @param <CONTEXT>
     * @return
     */
    public <CONTEXT extends IContextObject> PickElementResult<IFacadeCollection, ?> getCollection(String collectionName) {
        return contextExplorer.pickElement(collectionName, IFacadeCollection.class);
    }

    /**
     * Получение всех элементов коллекции
     *
     * @param collectionName - имя коллекции
     * @param <CONTEXT>
     * @return
     */
    @UIStep
    @TestStep("получить элементы коллекции \"${collectionName}\"")
    public <CONTEXT extends IContextObject> List<CONTEXT> getItems(String collectionName) {

        IFacadeCollection<CONTEXT> collection = getCollection(collectionName).getElement();

        List<CONTEXT> result = new ArrayList<>();

        collection.forEach(item -> {
            result.add(item);
        });

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
    public <CONTEXT extends IContextObject> List<CONTEXT> getItems(String collectionName, List<FindCondition> conditions, boolean onlyFirst) {

        IFacadeCollection<CONTEXT> collection = getCollection(collectionName).getElement();

        List<CONTEXT> result = new ArrayList<>();

        for (CONTEXT item : collection) {

            boolean match = true;

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
                    match = row.getOperator().checkValue(actual, expected);
                }

                if (!match) {
                    break;
                }
            }

            if (match) {
                result.add(item);
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

    /**
     * Класс, описывающий условия проверки сортировки по одному полю
     */
    @Data
    public static class SortParams {

        private Class clazz = null;
        private String pattern = null;
        private Character decimalSeparator = null;
        private Character groupingSeparator = null;
        private Boolean desc = false;
        private Boolean ignoreCase = false;

        public void check() {
            if (null == clazz) {
                fail(ERROR_MESSAGE_PREFIX + " не указан тип или задан неверно");
                return;
            }

            if (null == pattern && (Date.class == clazz || Double.class == clazz || Long.class == clazz)) {
                fail(ERROR_MESSAGE_PREFIX + " не указан шаблон");
                return;
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
            if (null == s | s.isEmpty()) {
                return null;
            }
            try {
                return getDateFormat().parse(s);
            } catch (ParseException e) {
                fail(ERROR_MESSAGE_PREFIX + " значение \"" + s + "\" не является датой");
                return null;
            }
        }

        private Double getDouble(String s) {
            if (null == s | s.isEmpty()) {
                return null;
            }
            Double d = null;
            try {
                return getDecimalFormat().parse(s).doubleValue();
            } catch (ParseException e) {
                fail(ERROR_MESSAGE_PREFIX + " значение \"" + s + "\" не является вещественным числом");
                return null;
            }
        }

        private Long getLong(String s) {
            if (null == s | s.isEmpty()) {
                return null;
            }
            try {
                return getDecimalFormat().parse(s).longValue();
            } catch (ParseException e) {
                fail(ERROR_MESSAGE_PREFIX + " значение \"" + s + "\" не является целым числом");
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
         *
         * @param s1
         * @param s2
         * @return
         */
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

            if (c1 == null && c2 != null) {
                return -1;
            } else if (c1 != null && c2 == null) {
                return 1;
            } else {
                return (desc ? -1 : 1) * c1.compareTo(c2);
            }
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

        try {
            array.stream().reduce((last, curr) -> {
                if (compare(last, curr, sortParamsMap) > 0) {
                    throw new UnsupportedOperationException();
                }
                return curr;
            });
        } catch (UnsupportedOperationException ex) {
            fail(String.format("Коллекция \"%s\" не отсортирована", collectionName));
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
     * @param collectionName
     * @param conditions
     */
    private void checkSortedInternal(String collectionName, List<Pair<String, String>> conditions) {

        Map<String, SortParams> sortParamsMap = new HashMap<>();
        SortParams sortParams = null;
        String groupField = null;

        for (Pair<String, String> entry : conditions) {

            String conditionField = entry.getKey();
            String contitionValue = entry.getValue();

            if (conditionField.equalsIgnoreCase("Поле")) {
                if (null != sortParams) {
                    sortParams.check();
                }
                sortParams = new SortParams();
                sortParamsMap.put(contitionValue, sortParams);
            } else if (conditionField.equalsIgnoreCase("тип")) {
                if (contitionValue.equalsIgnoreCase("строка")) {
                    sortParams.setClazz(String.class);
                } else if (contitionValue.equalsIgnoreCase("целое")) {
                    sortParams.setClazz(Long.class);
                } else if (contitionValue.equalsIgnoreCase("вещественное")) {
                    sortParams.setClazz(Double.class);
                } else if (contitionValue.equalsIgnoreCase("дата")) {
                    sortParams.setClazz(Date.class);
                }
            } else if (conditionField.equalsIgnoreCase("шаблон")) {
                sortParams.setPattern(contitionValue);
            } else if (conditionField.equalsIgnoreCase("разделитель дробной части")) {
                sortParams.setDecimalSeparator(contitionValue.charAt(0));
            } else if (conditionField.equalsIgnoreCase("разделитель групп")) {
                sortParams.setGroupingSeparator(contitionValue.charAt(0));
            } else if (conditionField.equalsIgnoreCase("порядок сортировки")) {
                if (contitionValue.equalsIgnoreCase("по убыванию")) {
                    sortParams.setDesc(true);
                }
            } else if (conditionField.equalsIgnoreCase("без учета заглавных букв")) {
                sortParams.setIgnoreCase(Boolean.parseBoolean(contitionValue));
            } else if (conditionField.equalsIgnoreCase("поле группировки")) {
                groupField = contitionValue;
            }

        }
        if (null != sortParams) {
            sortParams.check();
        }

        final List<IContextObject> items = this.getItems(collectionName);

        if (null == groupField) {
            // Получить значения из коллекции
            List<Map<String, String>> array = items.stream()
                    .map(item -> getRow(item, sortParamsMap))
                    .collect(Collectors.toList());

            checkSortArray(collectionName, array, sortParamsMap);

        } else {

            // Разбить проверяемую коллекцию на круппы
            String finalGroupField = groupField;
            Map<String, List<Map<String, String>>> groups = items.stream()
                    .collect(
                            Collectors.groupingBy(
                                    item -> {
                                        IFacadeReadable elItem = ((IContextObject) item).getField(finalGroupField);
                                        return elItem.getFieldValue();
                                    },
                                    Collectors.mapping(
                                            item -> getRow(item, sortParamsMap), Collectors.toList()
                                    )
                            )
                    );

            groups.values().forEach(list ->
                    checkSortArray(
                            collectionName,
                            list,
                            sortParamsMap)
            );

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

        conditions.add(0, Pair.of("Поле", field));
        checkSortedInternal(collectionName, conditions);

    }

}
