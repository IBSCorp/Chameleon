package ru.ibsqa.chameleon.json.context;

import ru.ibsqa.chameleon.context.IContextManager;

import java.io.InputStream;
import java.io.OutputStream;

public interface IContextManagerJson extends IContextManager<IJsonLookObject> {
    IJsonLookObject createJson(String lookName, String jsonValue);
    IJsonLookObject openJson(String lookName, InputStream is, String charsetName);
    void saveJson(OutputStream os, String charsetName);
    IJsonLookObject getCurrentJson();
    void clear();
}
