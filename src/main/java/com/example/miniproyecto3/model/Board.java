package com.example.miniproyecto3.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private static final int SIZE = 10;

    private final ArrayList<ArrayList<Boolean>> playerBoard = createEmptyBoard();
    private final ArrayList<ArrayList<Boolean>> enemyBoard = createEmptyBoard();
    private final ArrayList<ArrayList<Boolean>> shotsOnEnemyBoard = createEmptyBoard();
    private final ArrayList<ArrayList<Boolean>> shotsOnPlayerBoard = createEmptyBoard();
    private final ArrayList<ArrayList<Boolean>> sunkPlayerShips = createEmptyBoard();
    private final ArrayList<ArrayList<Boolean>> sunkEnemyShips = createEmptyBoard();


    private ArrayList<ArrayList<Boolean>> createEmptyBoard() {
        ArrayList<ArrayList<Boolean>> board = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            ArrayList<Boolean> row = new ArrayList<>();
            for (int j = 0; j < SIZE; j++) {
                row.add(false);
            }
            board.add(row);
        }
        return board;
    }

    public Ship placeShip(int row, int col, int size, boolean horizontal, boolean isPlayer) {
        if (size <= 0) {
            throw new IllegalArgumentException("El tamaño del barco debe ser mayor que cero.");
        }

        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE || (horizontal && col + size > SIZE) || (!horizontal && row + size > SIZE)) {
            throw new IndexOutOfBoundsException("El barco se sale del tablero.");
        }

        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            boolean occupied = isPlayer ? playerBoard.get(r).get(c) : enemyBoard.get(r).get(c);
            if (occupied) {
                throw new IllegalStateException("Ya existe un barco en la ubicación seleccionada.");
            }
        }

        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            if (isPlayer) {
                playerBoard.get(r).set(c, true);
            } else {
                enemyBoard.get(r).set(c, true);
            }
        }

        return new Ship(size, row, col, horizontal);
    }

    public void removeShip(Ship ship, boolean isPlayer) {
        for (Ship.Coordinate coordinate : ship.getCoordinates()) {
            int row = coordinate.getRow();
            int col = coordinate.getCol();
            if (isPlayer) {
                playerBoard.get(row).set(col, false);
                sunkPlayerShips.get(row).set(col, true);
            } else {
                enemyBoard.get(row).set(col, false);
                sunkEnemyShips.get(row).set(col, true);
            }
        }
    }


    public ArrayList<ArrayList<Boolean>> getPlayerBoard() {
        return playerBoard;
    }

    public ArrayList<ArrayList<Boolean>> getEnemyBoard() {
        return enemyBoard;
    }

    public ArrayList<ArrayList<Boolean>> getShotsOnEnemyBoard() {
        return shotsOnEnemyBoard;
    }

    public ArrayList<ArrayList<Boolean>> getShotsOnPlayerBoard() {
        return shotsOnPlayerBoard;
    }

    public ArrayList<ArrayList<Boolean>> getSunkPlayerShips() {
        return sunkPlayerShips;
    }

    public ArrayList<ArrayList<Boolean>> getSunkEnemyShips() {
        return sunkEnemyShips;
    }

    public boolean alreadyShotAt(int row, int col, boolean isPlayer) {
        return isPlayer ? shotsOnEnemyBoard.get(row).get(col) : shotsOnPlayerBoard.get(row).get(col);
    }

    public void registerShot(int row, int col, boolean isPlayer) {
        if (isPlayer) {
            shotsOnEnemyBoard.get(row).set(col, true);
        } else {
            shotsOnPlayerBoard.get(row).set(col, true);
        }
    }

    public boolean hasShipAt(int row, int col, boolean isPlayer) {
        return isPlayer ? playerBoard.get(row).get(col) : enemyBoard.get(row).get(col);
    }
}
