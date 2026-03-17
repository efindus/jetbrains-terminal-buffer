package com.efindus.api.cursor;

public class Cursor {
    private final int screenWidth;
    private final int screenHeight;

    private int currentX;
    private int currentY;
    public Cursor(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.currentX = 0;
        this.currentY = 0;
    }

    public Cursor(Cursor other) {
        this.screenWidth = other.screenWidth;
        this.screenHeight = other.screenHeight;
        this.currentX = other.currentX;
        this.currentY = other.currentY;
    }

    public int getX() {
        return currentX;
    }

    public int getY() {
        return currentY;
    }

    public void setXY(int x, int y) {
        this.currentX = clampX(x);
        this.currentY = clampY(y);
    }

    public void setX(int x) {
        this.currentX = clampX(x);
    }

    public void setY(int y) {
        this.currentY = clampY(y);
    }

    private int clampX(int x) {
        if (x < 0) return 0;
        if (x >= screenWidth) return screenWidth - 1;
        return x;
    }

    private int clampY(int y) {
        if (y < 0) return 0;
        if (y >= screenHeight) return screenHeight - 1;
        return y;
    }

    public Cursor moveBy(int dx, int dy) {
        currentX = clampX(currentX + dx);
        currentY = clampY(currentY + dy);
        return this;
    }

    public Cursor left() {
        return left(1);
    }

    public Cursor left(int n) {
        return moveBy(-n, 0);
    }

    public Cursor right() {
        return right(1);
    }

    public Cursor right(int n) {
        return moveBy(n, 0);
    }

    public Cursor up() {
        return up(1);
    }

    public Cursor up(int n) {
        return moveBy(0, -n);
    }

    public Cursor down() {
        return down(1);
    }

    public Cursor down(int n) {
        return moveBy(0, n);
    }

    /**
     * Advance cursor by one character
     * @return if false, advance overflew. scroll and put cursor at the first cell of the last row
     */
    public boolean advance() {
        return advance(1).overflowRows() == 0;
    }

    /**
     * @param n number of characters to advance by
     * @return if overflowRows bigger than 0 then advance buffer by that many rows and set XY to finalXY
     */
    public AdvanceResult advance(int n) {
        if (n < 0) throw new IllegalArgumentException("n must be >= 0");

        int target = currentY * screenWidth + currentX + n;
        int targetY = target / screenWidth;
        int targetX = target % screenWidth;

        if (targetY < screenHeight) {
            currentX = targetX;
            currentY = targetY;
            return new AdvanceResult(0, currentX, currentY);
        }

        int overflowRows = targetY - (screenHeight - 1);
        return new AdvanceResult(overflowRows, targetX, screenHeight - 1);
    }
}
