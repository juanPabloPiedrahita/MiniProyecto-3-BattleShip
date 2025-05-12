package com.example.miniproyecto3;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    public int size;
    public int startRow;
    public int startCol;
    public boolean horizontal;
    private boolean[] hits;

    public Ship(int size, int startRow, int startCol, boolean horizontal) {
        this.size = size;
        this.startRow = startRow;
        this.startCol = startCol;
        this.horizontal = horizontal;
        this.hits = new boolean[size];
    }

    public boolean isSunk() {
        for(boolean hit : hits) {
            if(!hit) return false;
        }
        return true;
    }

    public void registerHit(int row, int col) {
        for(int i = 0; i < size; i++) {
            int r = startRow + (horizontal ? 0 : i);
            int c = startCol + (horizontal ? i : 0);
            if(r == row && c == col) {
                hits[i] = true;
                break;
            }
        }
    }

    // Método para obtener las coordenadas que ocupa el barco en el tablero
    public List<int[]> getCoordinates() {
        List<int[]> coordinates = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int r = startRow + (horizontal ? 0 : i);
            int c = startCol + (horizontal ? i : 0);
            coordinates.add(new int[]{r, c});
        }
        return coordinates;
    }

    public boolean occupies(int row, int col) {
        for(int i = 0; i < size; i++) {
            int r = startRow + (horizontal ? 0 : i);
            int c = startCol + (horizontal ? i : 0);
            if(r == row && c == col) {
                return true;
            }
        }
        return false;
    }

    // Método para verificar si una coordenada está ocupada por el barco
    public boolean isOccupied(int row, int col) {
        for (int[] coord : getCoordinates()) {
            if (coord[0] == row && coord[1] == col) {
                return true;
            }
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public boolean isHorizontal() {
        return horizontal;
    }
}
