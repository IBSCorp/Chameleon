package ru.ibsqa.qualit.influx.context;

import ru.ibsqa.qualit.influx.providers.FieldData;
import ru.ibsqa.qualit.influx.providers.TagData;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class InfluxEntry {

    @Getter
    private Map<String, Object> fields = new LinkedHashMap<>();

    @Getter
    private Map<String, String> tags = new LinkedHashMap<>();

    public void addField(FieldData fieldData) {
        if (Objects.nonNull(fieldData)) {
            addField(fieldData.getFieldName(), fieldData.getValue());
        }
    }

    public InfluxEntry addField(String name, Object value) {

        if (fields.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Поле [%s] уже существует", name));
        }

        fields.put(name, value);
        return this;
    }

    public void addTag(TagData tagData) {
        if (Objects.nonNull(tagData)) {
            addTag(tagData.getTagName(), tagData.getValue());
        }
    }

    public InfluxEntry addTag(String name, String value) {

        if (tags.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Тэг [%s] уже существует", name));
        }

        tags.put(name, value);
        return this;
    }

}
