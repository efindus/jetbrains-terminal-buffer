package com.efindus;

import com.efindus.api.CellStyle;
import com.efindus.utils.RingList;

record InsertData(char c, CellStyle s) {}

@SuppressWarnings("UnusedReturnValue")
public class TerminalBuffer {
    private final int screenWidth;
    private final int screenHeight;

    private final RingList<TerminalRow> screen;
    private final RingList<TerminalRow> scrollback;

    private final CellStyle currentStyle;
    private final Cursor cursor;

    public TerminalBuffer(int width, int height, int scrollbackLines) {
        screenWidth = width;
        screenHeight = height;
        screen = new RingList<>(height, height, () -> new TerminalRow(width));
        scrollback = new RingList<>(scrollbackLines, () -> new TerminalRow(width));
        currentStyle = new CellStyle();
        cursor = new Cursor(width, height);
    }

    public CellStyle getStyle() {
        return currentStyle;
    }

    public TerminalBuffer write(String content) {
        return write(content.toCharArray());
    }

    public TerminalBuffer write(char[] content) {
        if (content == null) throw new IllegalArgumentException("content must not be null");
        if (content.length == 0) return this;

        int pos = 0;
        while (pos < content.length) {
            TerminalRow r = screen.get(cursor.getY());
            int prevPos = pos;
            pos = r.write(content, pos, cursor.getX(), currentStyle);
            advance(pos - prevPos);
        }

        return this;
    }

    public TerminalBuffer insert(String content) {
        return insert(content.toCharArray());
    }

    public TerminalBuffer insert(char[] content) {
        if (content == null) throw new IllegalArgumentException("content must not be null");
        if (content.length == 0) return this;

        CellStyle cStyleCopy = new CellStyle(currentStyle);
        RingList<InsertData> writeBuffer = new RingList<>(content.length, () -> null);
        for (char c : content)
            writeBuffer.insert(new InsertData(c, cStyleCopy));

        while (writeBuffer.size() > 0) {
            var x = writeBuffer.poll();

            TerminalRow r = screen.get(cursor.getY());
            if (r.getCharAt(getX()) != null)
                writeBuffer.insert(new InsertData(r.getCharAt(getX()), r.getStyleAt(getX())));

            r.write(x.c(), getX(), x.s());
            advance();
        }

        return this;
    }

    public TerminalBuffer fillLine(char character) {
        TerminalRow r = screen.get(cursor.getY());
        r.write(character, screenWidth, 0, currentStyle);

        return this;
    }

    public TerminalBuffer clearLine() {
        return fillLine('\0');
    }

    public TerminalBuffer appendEmptyLine() {
        scrollback.insert(screen.scroll());
        moveBy(0, -1); // keep cursor in the same spot
        return this;
    }

    public TerminalBuffer clearScreen() {
        screen.clear();
        return this;
    }

    public TerminalBuffer clear() {
        screen.clear();
        scrollback.clear();
        return this;
    }

    public int getScrollbackSize() {
        return scrollback.size();
    }

    public Character getCharAt(int x, int y) {
        if (x < 0 || x >= screenWidth || y < -scrollback.size() || y >= screenHeight)
            throw new IndexOutOfBoundsException("x=" + x + " y=" + y);

        if (y >= 0)
            return screen.get(y).getCharAt(x);
        else
            return scrollback.get(scrollback.size() - y).getCharAt(x);
    }

    public CellStyle getStyleAt(int x, int y) {
        if (x < 0 || x >= screenWidth || y < -scrollback.size() || y >= screenHeight)
            throw new IndexOutOfBoundsException("x=" + x + " y=" + y);

        if (y >= 0)
            return new CellStyle(screen.get(y).getStyleAt(x));
        else
            return new CellStyle(scrollback.get(scrollback.size() + y).getStyleAt(x));
    }

    public String getLineAsString(int y) {
        if (y < -scrollback.size() || y >= screenHeight)
            throw new IndexOutOfBoundsException("y=" + y);

        if (y >= 0)
            return screen.get(y).asString();
        else
            return scrollback.get(scrollback.size() + y).asString();
    }

    public String getScreen() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < screenHeight; i++) {
            screen.get(i).writeLineIntoStringBuilder(sb);
            sb.append("\n");
        }

        return sb.toString();
    }

    public String getBuffer() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < scrollback.size(); i++) {
            scrollback.get(i).writeLineIntoStringBuilder(sb);
            sb.append("\n");
        }

        for (int i = 0; i < screen.size(); i++) {
            screen.get(i).writeLineIntoStringBuilder(sb);
            sb.append("\n");
        }

        return sb.toString();
    }

    // cursor fluent API
    public int getX() {
        return cursor.getX();
    }

    public int getY() {
        return cursor.getY();
    }

    public TerminalBuffer moveBy(int dx, int dy) {
        cursor.moveBy(dx, dy);
        return this;
    }

    public TerminalBuffer left() {
        return moveBy(-1, 0);
    }

    public TerminalBuffer left(int n) {
        return moveBy(-n, 0);
    }

    public TerminalBuffer right() {
        return moveBy(1, 0);
    }

    public TerminalBuffer right(int n) {
        return moveBy(n, 0);
    }

    public TerminalBuffer up() {
        return moveBy(0, -1);
    }

    public TerminalBuffer up(int n) {
        return moveBy(0, -n);
    }

    public TerminalBuffer down() {
        return moveBy(0, 1);
    }

    public TerminalBuffer down(int n) {
        return moveBy(0, n);
    }

    public TerminalBuffer setXY(int x, int y) {
        cursor.setXY(x, y);
        return this;
    }

    public TerminalBuffer setX(int x) {
        cursor.setX(x);
        return this;
    }

    public TerminalBuffer setY(int y) {
        cursor.setY(y);
        return this;
    }

    public TerminalBuffer advance() {
        return advance(1);
    }

    public TerminalBuffer advance(int n) {
        if (n < 0) throw new IllegalArgumentException("n must be >= 0");

        int target = getY() * screenWidth + getX() + n;
        int targetY = target / screenWidth;
        int targetX = target % screenWidth;

        if (targetY < screenHeight) {
            setX(targetX);
            setY(targetY);
            return this;
        }

        int overflowRows = targetY - (screenHeight - 1);
        for (int i = 0; i < overflowRows; i++)
            scrollback.insert(screen.scroll());

        setX(targetX);
        setY(screenHeight - 1);
        return this;
    }
}
