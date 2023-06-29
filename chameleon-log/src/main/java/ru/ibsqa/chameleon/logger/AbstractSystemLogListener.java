package ru.ibsqa.chameleon.logger;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

public abstract class AbstractSystemLogListener extends OutputStream {

    private final PrintStream originalSystemOut;
    private final boolean isError;

    @Autowired
    private ILogRender logRender;

    public AbstractSystemLogListener(PrintStream stdout, boolean isError) {
        this.originalSystemOut = stdout;
        this.isError = isError;
        PrintStream printStream = new PrintStream(this, true);
        if (isError) {
            System.setErr(printStream);
        } else {
            System.setOut(printStream);
        }
    }

    private StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void write(int b) throws IOException {
        byte bytes[] = {(byte) b};
        stringBuilder.append(new String(bytes, Charset.defaultCharset()));
        String[] lines = stringBuilder.toString().split("\r");
        if (lines.length > 1) {
            logRender.println(isError, lines[0]);
            stringBuilder.setLength(0);
        }

        originalSystemOut.write(b);
    }

}
