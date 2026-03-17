package com.efindus.api;

import java.util.EnumSet;
import java.util.Objects;

public class CellStyle {
    private TerminalColor fgColor;
    private TerminalColor bgColor;
    private EnumSet<TerminalAttribute> attribs;

    public TerminalColor getFgColor() {
        return fgColor;
    }

    public void setFgColor(TerminalColor fgColor) {
        this.fgColor = fgColor;
    }

    public TerminalColor getBgColor() {
        return bgColor;
    }

    public void setBgColor(TerminalColor bgColor) {
        this.bgColor = bgColor;
    }

    public EnumSet<TerminalAttribute> getAttribs() {
        return attribs;
    }

    public void setAttribs(EnumSet<TerminalAttribute> attribs) {
        this.attribs = attribs;
    }

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