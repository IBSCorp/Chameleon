package ru.ibsqa.chameleon.recorders;

public interface IVideoRecorderConfiguration {

    void setExtensionPath(String path);

    String getExtensionPath();

    void setExtensionId(String id);

    String getExtensionId();

    boolean isEnabled();

}
