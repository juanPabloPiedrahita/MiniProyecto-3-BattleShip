package com.example.miniproyecto3;

public class Ship {
    public int size;
    public int startRow;
    public int startCol;
    public boolean horizontal;

    public Ship(int size, int startRow, int startCol, boolean horizontal) {
        this.size = size;
        this.startRow = startRow;
        this.startCol = startCol;
        this.horizontal = horizontal;
    }
}
