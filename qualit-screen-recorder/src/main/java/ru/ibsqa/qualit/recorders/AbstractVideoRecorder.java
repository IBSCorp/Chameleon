package ru.ibsqa.qualit.recorders;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

public abstract class AbstractVideoRecorder implements IVideoRecorder{

    @Getter @Setter
    protected IVideoRecorderConfiguration videoRecorderConfiguration;

    protected abstract byte[] attachVideo(File file);
}
