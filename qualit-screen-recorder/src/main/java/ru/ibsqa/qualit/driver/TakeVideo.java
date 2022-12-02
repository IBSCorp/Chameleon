package ru.ibsqa.qualit.driver;

import java.io.File;

public interface TakeVideo {
    void startVideo();
    File stopVideo(boolean save);
}
