package com.efindus;

import com.efindus.api.CellStyle;
import com.efindus.api.cursor.Cursor;
import com.efindus.utils.RingList;

class TerminalRow {
    char[] chars;
    CellStyle[] styles;

    TerminalRow(int width) {
        chars = new char[width];
        styles = new CellStyle[width];

        for  (int i = 0; i < width; i++) {
            chars[i] = '\0';
            styles[i] = new CellStyle();
        }
    }
}

public class TerminalBuffer {
    private final RingList<TerminalRow> screen;
    private final RingList<TerminalRow> scrollback;

    private final CellStyle currentStyle;
    private final Cursor cursor;

    public TerminalBuffer(int width, int height, int scrollbackLines) {
        screen = new RingList<>(height, height, () -> new TerminalRow(width));
        scrollback = new RingList<>(scrollbackLines, () -> new TerminalRow(width));
        currentStyle = new CellStyle();
        cursor = new Cursor(width, height);
    }

    public CellStyle getStyle() {
        return currentStyle;
    }

    public Cursor getCursor() {
        return cursor;
    }
}
