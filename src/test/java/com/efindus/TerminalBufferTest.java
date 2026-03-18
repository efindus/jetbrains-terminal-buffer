package com.efindus;

import com.efindus.api.CellStyle;
import com.efindus.api.TerminalAttribute;
import com.efindus.api.TerminalColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class TerminalBufferTest {
    TerminalBuffer tb;

    @BeforeEach
    void setUp() {
        tb = new TerminalBuffer(8, 4, 8);
    }

    @Test
    void basicWriting() {
        tb.write("Test");
        assertEquals("Test", tb.getLineAsString(0));
    }

    @Test
    void basicWriting2() {
        tb.right().write("Tes").right().write("xx ");
        assertEquals("Tes xx ", tb.getLineAsString(0));
    }

    @Test
    void cursorClamping() {
        tb.left(1000).up(1000).down()
                .write("Test")
                .down(1000).right(1000).left(2)
                .write("xx ")
                .setX(-10).moveBy(0, -1)
                .write(".");

        assertEquals("Test", tb.getLineAsString(0));
        // full-width write advances the cursor to the next row, scrolling content
        assertEquals(".    xx ", tb.getLineAsString(2));
        assertEquals("", tb.getLineAsString(3));
    }

    @Test
    void cursorClamping2() {
        assertThrows(IllegalArgumentException.class, () -> tb.advance(-1));
        tb.up().left().write(".")
                .setXY(10000, 10000).advance().write("Test");

        assertEquals(".", tb.getLineAsString(-1));
        assertEquals("Test", tb.getLineAsString(3));
    }

    @Test
    void accessorBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> tb.getLineAsString(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> tb.getLineAsString(4));
        assertThrows(IndexOutOfBoundsException.class, () -> tb.getCharAt(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> tb.getCharAt(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> tb.getCharAt(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> tb.getCharAt(8, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> tb.getStyleAt(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> tb.getStyleAt(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> tb.getStyleAt(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> tb.getStyleAt(8, 0));

        tb.write("Test").advance(4 * 8);

        assertEquals("Test", tb.getLineAsString(-1));
        assertEquals('T', tb.getCharAt(0, -1));
        assertEquals(tb.getStyle(), tb.getStyleAt(0, -1));
        assertNull(tb.getCharAt(0, 0));
        assertNull(tb.getStyleAt(0, 0));
    }

    @Test
    void clearing() {
        tb.write("Test").advance(4 * 8).setXY(0, 0).write("XXXX");
        assertEquals("""
        Test
        XXXX
        
        
        
        """.stripIndent(), tb.getBuffer());

        tb.clearScreen();
        assertEquals(1, tb.getScrollbackSize());
        assertEquals("""
        Test
        
        
        
        
        """.stripIndent(), tb.getBuffer());
        assertEquals("""
        
        
        
        
        """.stripIndent(), tb.getScreen());

        tb.clear();
        assertEquals("""
        
        
        
        
        """.stripIndent(), tb.getBuffer());
    }

    @Test
    void wrappingWriting() {
        tb.write("T").right(1000).write("xx ");
        assertEquals("T      x", tb.getLineAsString(0));
        assertEquals("x ", tb.getLineAsString(1));
    }

    @Test
    void scrolling() {
        tb.down().write("Test");
        assertEquals("Test", tb.getLineAsString(1));

        tb.appendEmptyLine();
        assertEquals("Test", tb.getLineAsString(0));
        tb.clearLine();
        assertEquals("", tb.getLineAsString(0));
        tb.fillLine('x').write("Test");
        assertEquals("xxxxTest", tb.getLineAsString(0));
    }

    @Test
    void inserting() {
        tb
                .right().write("x")
                .right().write("y")
                .right().write("z")
                .down().setX(0).write("abc");

        assertEquals("""
        x y z
        abc
        
        
        """.stripIndent(), tb.getScreen());

        tb.setXY(0, 0).insert("testtest");
        assertEquals("""
        testtest
        xyzabc
        
        
        """.stripIndent(), tb.getScreen());

        tb.insert("testtes1" + "testtes2" + "testtes3" + "testtes4");
        assertEquals("""
        testtest
        testtes1
        testtes2
        testtes3
        testtes4
        xyzabc
        """.stripIndent(), tb.getBuffer());

        assertEquals(0, tb.getX());
        assertEquals(3, tb.getY());
    }

    @Test
    void inserting2() {
        tb.write("testtes1" + "testtes2" + "testtes3" + "testtes");
        tb.setXY(0, 0).insert("te");

        assertEquals(0, tb.getX());
        assertEquals(0, tb.getY());
    }

    @Test
    void styling() {
        tb.write("xxxx");
        CellStyle oldS = tb.getStyleAt(0, 0);

        tb.getStyle().setBgColor(TerminalColor.BLUE);
        tb.getStyle().setFgColor(TerminalColor.RED);
        tb.getStyle().getAttribs().add(TerminalAttribute.UNDERLINE);

        tb.setX(1).write("yy");
        assertEquals(tb.getStyle(), tb.getStyleAt(1, 0));
        assertNotEquals(oldS, tb.getStyleAt(1, 0));

        assertEquals(TerminalColor.RED, tb.getStyleAt(1, 0).getFgColor());
        assertEquals(TerminalColor.BLUE, tb.getStyleAt(1, 0).getBgColor());

        tb.getStyle().reset();
        assertEquals(oldS, tb.getStyle());
    }

    @Test
    void styling2() {
        EnumSet<TerminalAttribute> es = EnumSet.noneOf(TerminalAttribute.class);
        tb.getStyle().setAttribs(es);
        es.add(TerminalAttribute.UNDERLINE);

        tb.write("xxxx").write("").insert("");
        assertTrue(tb.getStyleAt(0, 0).getAttribs().contains(TerminalAttribute.UNDERLINE));
        es.clear();
        assertTrue(tb.getStyleAt(0, 0).getAttribs().contains(TerminalAttribute.UNDERLINE));

        CellStyle copy = new CellStyle(tb.getStyle());
        tb.getStyle().reset();
        assertEquals(copy, tb.getStyle());
    }

    @Test
    void writingNulls() {
        assertThrows(IllegalArgumentException.class, () -> {
            tb.write((String) null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            tb.write((char[]) null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            tb.insert((String) null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            tb.insert((char[]) null);
        });
    }
}