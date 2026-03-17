package com.efindus;

import com.efindus.api.CellStyle;

class TerminalRow {
    char[] chars;
    CellStyle[] styles;

    TerminalRow(int width) {
        chars = new char[width];
        styles = new CellStyle[width];

        for (int i = 0; i < width; i++) {
            chars[i] = '\0';
            styles[i] = new CellStyle();
        }
    }

    /**
     * @param content buffer to write from
     * @param start starting position from the buffer
     * @param writeAt position to start writing at
     * @param ref style to write the text in, makes a copy of the style before writing
     * @return index of first element not written into the row
     */
    int write(char[] content, int start, int writeAt, CellStyle ref) {
        CellStyle style = new CellStyle(ref);
        int index = start;
        for (int i = writeAt; i < chars.length && index < content.length; i++) {
            chars[i] = content[index++];
            styles[i] = style;
        }

        return index;
    }

    /**
     * @param c character to write
     * @param count number of characters to write
     * @param writeAt position to start writing at
     * @param ref style to write the text in, makes a copy of the style before writing
     * @return number of characters written
     */
    int write(char c, int count, int writeAt, CellStyle ref) {
        CellStyle style = new CellStyle(ref);
        int index = 0;
        for (int i = writeAt; i < chars.length; i++) {
            chars[i] = c;
            styles[i] = style;
            index++;
        }

        return index;
    }
}
