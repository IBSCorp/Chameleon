package ru.ibsqa.chameleon.recorders;

import java.io.File;

public interface IVideoRecorder {
    void start();
    File stop(boolean save);

    IVideoRecorderConfiguration getVideoRecorderConfiguration();
    void setVideoRecorderConfiguration(IVideoRecorderConfiguration videoRecorderConfiguration);
}
