package ru.ibsqa.qualit.logger;

public interface ILogRender {
    void println(boolean isError, String message);

    void hide();

    void show();
}
