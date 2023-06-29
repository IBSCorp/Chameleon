package ru.ibsqa.chameleon.logger;

import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Отрисовщик лога на базе прозрачного окна AWT
 */
public class TransparentWindow implements ILogRender {

    private Window window;
    private final List<LogItem> buffer = new ArrayList<>();
    private Font font;

    private int padding = 2;

    @Getter
    @Setter
    private boolean disabled = false;

    @Getter
    @Setter
    private float fontSize = 24f;

    @Getter
    @Setter
    private Color borderColor = null;

    @Getter
    @Setter
    private Color infoColor = Color.BLUE;

    @Getter
    @Setter
    private Color errorColor = Color.RED;

    @Getter
    @Setter
    private int indentLeft = 0;

    @Getter
    @Setter
    private int indentTop = 0;

    @Getter
    @Setter
    private int indentRight = 0;

    @Getter
    @Setter
    private int indentBottom = 0;

    @PostConstruct
    private void init() {
        if (!isDisabled()) {
            window = new Window(null) {

                @Override
                public void paint(Graphics g) {
                    if (Objects.nonNull(borderColor)) {
                        g.setColor(borderColor);

                    }
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

                    g.setFont(font);
                    FontMetrics metrics = g.getFontMetrics();
                    int rowCount = (getHeight() - 2 * padding) / metrics.getHeight();
                    int y = 0;

                    int startRow = buffer.size() - rowCount;
                    if (startRow < 0) {
                        startRow = 0;
                    }
                    List<LogItem> drawBuffer = new ArrayList<>();
                    for (int i = startRow; i < buffer.size(); i++) {
                        wrapLineInto(buffer.get(i), drawBuffer, metrics, getWidth() - 2 * padding);
                    }

                    startRow = drawBuffer.size() - rowCount;
                    if (startRow < 0) {
                        startRow = 0;
                    }
                    for (int i = startRow; i < drawBuffer.size(); i++) {
                        String line = drawBuffer.get(i).message;
                        g.setColor(drawBuffer.get(i).isError ? errorColor : infoColor);
                        y += metrics.getHeight();
                        g.drawString(line, padding, y + padding);
                    }
                }

                @Override
                public void update(Graphics g) {
                    paint(g);
                }
            };
            window.setAlwaysOnTop(true);
            resize();
            window.setBackground(new Color(0, true));
            window.setVisible(true);
            font = window.getFont().deriveFont(fontSize);
        }
    }

    /**
     * Given a line of text and font metrics information, wrap the line and add
     * the new line(s) to <var>list</var>.
     *
     * @param item     a line of text
     * @param list     an output list of strings
     * @param fm       font metrics
     * @param maxWidth maximum width of the line(s)
     */
    private static void wrapLineInto(LogItem item, List<LogItem> list, FontMetrics fm, int maxWidth) {
        String line = item.message;
        int len = line.length();
        int width;
        while (len > 0 && (width = fm.stringWidth(line)) > maxWidth) {
            // Guess where to split the line. Look for the next space before
            // or after the guess.
            int guess = len * maxWidth / width;
            String before = line.substring(0, guess).trim();

            width = fm.stringWidth(before);
            int pos;
            if (width > maxWidth) // Too long
                pos = findBreakBefore(line, guess);
            else { // Too short or possibly just right
                pos = findBreakAfter(line, guess);
                if (pos != -1) { // Make sure this doesn't make us too long
                    before = line.substring(0, pos).trim();
                    if (fm.stringWidth(before) > maxWidth)
                        pos = findBreakBefore(line, guess);
                }
            }
            if (pos == -1)
                pos = guess; // Split in the middle of the word

            list.add(new LogItem(item.isError, line.substring(0, pos).trim()));
            line = line.substring(pos).trim();
            len = line.length();
        }
        if (len > 0)
            list.add(new LogItem(item.isError, line));
    }

    /**
     * Returns the index of the first whitespace character or '-' in <var>line</var>
     * that is at or before <var>start</var>. Returns -1 if no such character is
     * found.
     *
     * @param line  a string
     * @param start where to star looking
     */
    public static int findBreakBefore(String line, int start) {
        for (int i = start; i >= 0; --i) {
            char c = line.charAt(i);
            if (Character.isWhitespace(c) || c == '-')
                return i;
        }
        return -1;
    }

    /**
     * Returns the index of the first whitespace character or '-' in <var>line</var>
     * that is at or after <var>start</var>. Returns -1 if no such character is
     * found.
     *
     * @param line  a string
     * @param start where to star looking
     */
    public static int findBreakAfter(String line, int start) {
        int len = line.length();
        for (int i = start; i < len; ++i) {
            char c = line.charAt(i);
            if (Character.isWhitespace(c) || c == '-')
                return i;
        }
        return -1;
    }

    private void resize() {
        Rectangle bounds = window.getGraphicsConfiguration().getBounds();
        int x = bounds.width * indentLeft / 100;
        int y = bounds.height * indentTop / 100;
        int width = bounds.width - bounds.width * (indentLeft + indentRight) / 100;
        int height = bounds.height - bounds.height * (indentTop + indentBottom) / 100;
        window.setBounds(x, y, width, height);
    }

    @Override
    public void println(boolean isError, String message) {
        if (!isDisabled()) {
            buffer.add(new LogItem(isError, message));
            window.setVisible(false);
            window.setVisible(true);
        }
    }

    @Override
    public void hide() {
        if (!isDisabled()) {
            window.setVisible(false);
        }
    }

    @Override
    public void show() {
        if (!isDisabled()) {
            window.setVisible(true);
        }
    }

    @PostConstruct
    private void quit() {
        if (!isDisabled() && Objects.nonNull(window)) {
            window.dispose();
        }
    }

    static class LogItem {
        boolean isError;
        String message;

        LogItem(boolean isError, String message) {
            this.isError = isError;
            this.message = message;
        }
    }

}
