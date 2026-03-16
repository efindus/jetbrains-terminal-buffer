package com.efindus.api;

import java.util.EnumSet;
import java.util.Objects;

public class CellStyle {
    TerminalColor fgColor;
    TerminalColor bgColor;
    EnumSet<TerminalAttribute> attribs;

    public CellStyle() {
        fgColor = TerminalColor.BRIGHT_WHITE;
        bgColor = TerminalColor.BLACK;
        attribs = EnumSet.noneOf(TerminalAttribute.class);
    }

    public CellStyle(CellStyle other) {
        this.fgColor = other.fgColor;
        this.bgColor = other.bgColor;
        this.attribs = EnumSet.copyOf(other.attribs);
    }

    public void reset() {
        fgColor = TerminalColor.BRIGHT_WHITE;
        bgColor = TerminalColor.BLACK;
        attribs.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellStyle that)) return false;
        return fgColor == that.fgColor
                && bgColor == that.bgColor
                && attribs.equals(that.attribs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fgColor, bgColor, attribs);
    }
}