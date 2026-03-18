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

    void write(char c, int writeAt, CellStyle s) {
        chars[writeAt] = c;
        styles[writeAt] = s;
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
        for (int i = writeAt; i < chars.length && index < count; i++) {
            chars[i] = c;
            styles[i] = style;
            index++;
        }

        return index;
    }

    Character getCharAt(int index) {
        if (chars[index] == '\0') return null;
        return chars[index];
    }

    CellStyle getStyleAt(int index) {
        if (chars[index] == '\0') return null;
        return styles[index];
    }

    void writeLineIntoStringBuilder(StringBuilder sb) {
        // trim line
        int end = chars.length - 1;
        while (end >= 0 && chars[end] == '\0') end--;

        int start = 0;
        while (start <= end && chars[start] == '\0') start++;

        for (int i = start; i <= end; i++) {
            if (chars[i] == '\0') sb.append(' ');
            else sb.append(chars[i]);
        }
    }

    String asString() {
        StringBuilder sb = new StringBuilder();
        writeLineIntoStringBuilder(sb);

        return sb.toString();
    }
}
