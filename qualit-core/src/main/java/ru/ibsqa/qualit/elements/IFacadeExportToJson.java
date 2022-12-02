package ru.ibsqa.qualit.elements;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.fail;

public interface IFacadeExportToJson<JSON> extends IFacadeReadable {
    JSON exportToJson() throws JSONException;

    default boolean isFieldExists() {
        return true;
    }

    default String getFieldValue() {
        Object json = null;
        try {
            json = exportToJson();
        } catch (JSONException e) {
            fail("Ошибка экспорта в JSON");
        }
        if (json instanceof JSONObject) {
            return json.toString();
        } else if (json instanceof JSONArray) {
            return json.toString();
        } else {
            return "";
        }
    }

}
