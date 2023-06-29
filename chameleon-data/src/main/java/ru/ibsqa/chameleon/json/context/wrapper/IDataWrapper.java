package ru.ibsqa.chameleon.json.context.wrapper;

public interface IDataWrapper {

    /**
     * Установить данные json или xml
     * @return
     */
    void setDataValue(String value);

    /**
     * Получить весь json или xml
     * @return
     */
    default String getDataValue() {
        return getDataValue(null);
    }

    /**
     * Получить фрагмент json или xml
     * @return
     */
    String getDataValue(String path);

    /**
     * Получить json или xml в форматированном виде
     * @return
     */
    default String getDataPretty() {
        return getDataPretty(null);
    }

    /**
     * Получить фрагмент json или xml в форматированном виде
     * @return
     */
    String getDataPretty(String path);

    /**
     * Проверка наличия поля
     * @param path
     * @return
     */
    boolean isExists(String path);

    /**
     * Установка значения существующего поля
     * @param path
     * @param value
     * @throws PathException
     */
    void set(String path, Object value) throws PathException;

    /**
     * Установка значения существующего или добавление нового поля
     * @param path
     * @param value
     * @throws PathException
     */
    void create(String path, Object value);

    /**
     * Удаление существующего поля
     * @param path
     * @throws PathException
     */
    void delete(String path) throws PathException;

    /**
     * Добавление нового поля в массив
     * @param path
     * @param value
     * @throws PathException
     */
    void add(String path, Object value);

    /**
     * Считываем значение поля
     * @param path
     * @param type
     * @param <T>
     * @return
     * @throws PathException
     */
    <T> T read(String path, Class<T> type) throws PathException;

    /**
     * Валидируем json по схеме
     * @param schema
     */
    void validateSchema(String schema);

    /**
     * Получение размера массива или количество полей объекта. В иных случаях возвращается -1
     * @param path
     * @return
     */
    long getDataLength(String path);

}
