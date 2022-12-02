package ru.ibsqa.qualit.recorders;

import com.google.common.io.Files;
import ru.ibsqa.qualit.reporter.TestAttachment;
import lombok.extern.slf4j.Slf4j;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

@Slf4j
public class DesktopVideoRecorder extends AbstractVideoRecorder {
    @Autowired
    private IDriverManager driverManager;

    private String scenarioName = "asdfasdf";
    private File record;
    private boolean recording;
    private ScreenRecorder screenRecorder;
    private static final String DIRECTORY = String.format("%s\\target\\videograb\\", System.getProperty("basedir"));

    @Override
    public void start() {
        try {
            GraphicsConfiguration gc = GraphicsEnvironment
                    .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                    .getDefaultConfiguration();

            File dir = new File(DIRECTORY);

            Point point = driverManager.getLastDriver().manage().window().getPosition();
            Dimension dimension = driverManager.getLastDriver().manage().window().getSize();
            Rectangle rectangle = new Rectangle(point.x, point.y,
                    dimension.width, dimension.height);

            screenRecorder = new ScreenRecorder(gc,
                    rectangle,
                    new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey,
                            MIME_AVI),
                    new Format(MediaTypeKey, FormatKeys.MediaType.VIDEO, EncodingKey,
                            ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey,
                            COMPRESSOR_NAME_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey,
                            24, FrameRateKey, Rational.valueOf(10), QualityKey,
                            1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey,
                            "black", FrameRateKey, Rational.valueOf(30)),
                    null,
                    dir);
            screenRecorder.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public File stop(boolean save) {
        try {
            screenRecorder.stop();
            if (scenarioName != null) {
                File newFileName = new File(String.format("%s%s.avi",
                        DIRECTORY, scenarioName));
                screenRecorder.getCreatedMovieFiles().get(0)
                        .renameTo(newFileName);
                record = newFileName;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (save){
            attachVideo(record);
        }
        return record;
    }

    @Override
    @TestAttachment(value = "VIDEO", mimeType = "video/avi")
    protected byte[] attachVideo(File file) {
        try {
            return Files.toByteArray(file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return new byte[0];
        }
    }
}
