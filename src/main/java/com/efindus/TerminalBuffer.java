package com.efindus;

import com.efindus.api.CellStyle;

import java.util.*;

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
    private int screenTop;
    private final TerminalRow[] screen;

    int scrollbackTop;
    int scrollbackSize;
    private final TerminalRow[] scrollback;

    private CellStyle currentStyle;

    public TerminalBuffer(int width, int height, int scrollbackLines) {
        screen = new TerminalRow[height];
        screenTop = 0;

        for (int i = 0; i < height; i++)
            screen[i] = new TerminalRow(width);

        scrollback = new TerminalRow[scrollbackLines];
        scrollbackTop = 0;
        scrollbackSize = 0;

        currentStyle = new CellStyle();
    }
}
