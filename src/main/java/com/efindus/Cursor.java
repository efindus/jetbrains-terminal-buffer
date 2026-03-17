package com.efindus;

class Cursor {
    private int x;
    private int y;
    private final int screenWidth;
    private final int screenHeight;

    public Cursor(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.x = 0;
        this.y = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setXY(int x, int y) {
        this.x = clampX(x);
        this.y = clampY(y);
    }

    public void setX(int x) {
        this.x = clampX(x);
    }

    public void setY(int y) {
        this.y = clampY(y);
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

    public void moveBy(int dx, int dy) {
        x = clampX(x + dx);
        y = clampY(y + dy);
    }
}
