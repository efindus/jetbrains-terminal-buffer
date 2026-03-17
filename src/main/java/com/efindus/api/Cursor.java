package com.efindus.api;

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

    
}
