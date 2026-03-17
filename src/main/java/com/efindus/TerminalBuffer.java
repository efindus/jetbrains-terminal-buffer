package com.efindus;

import com.efindus.api.CellStyle;
import com.efindus.utils.RingList;

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
