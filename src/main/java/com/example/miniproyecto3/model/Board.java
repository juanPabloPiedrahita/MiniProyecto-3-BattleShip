package com.example.miniproyecto3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
    private static final int SIZE = 10;

    private ArrayList<ArrayList<Boolean>> playerBoard = createEmptyBoard();
    private ArrayList<ArrayList<Boolean>> enemyBoard = createEmptyBoard();
    private ArrayList<ArrayList<Boolean>> shotsOnEnemyBoard = createEmptyBoard();
    private ArrayList<ArrayList<Boolean>> shotsOnPlayerBoard = createEmptyBoard();

    private List<Ship> playerShips = new ArrayList<>();
    private List<Ship> enemyShips = new ArrayList<>();

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
        boolean canPlace = true;

        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            if (r >= SIZE || c >= SIZE || (isPlayer ? playerBoard.get(r).get(c) : enemyBoard.get(r).get(c))) {
                canPlace = false;
                break;
            }
        }

        if (canPlace) {
            for (int i = 0; i < size; i++) {
                int r = row + (horizontal ? 0 : i);
                int c = col + (horizontal ? i : 0);
                if (isPlayer) {
                    playerBoard.get(r).set(c, true);
                } else {
                    enemyBoard.get(r).set(c, true);
                }
            }
            Ship ship = new Ship(size, row, col, horizontal);
            if (isPlayer) {
                playerShips.add(ship);
            } else {
                enemyShips.add(ship);
            }
            return ship;
        }
        return null;
    }

    public void removeShip(Ship ship, boolean isPlayer) {
        for (Ship.Coordinate coordinate : ship.getCoordinates()) {
            int row = coordinate.getRow();
            int col = coordinate.getCol();
            if (isPlayer) {
                playerBoard.get(row).set(col, false);
            } else {
                enemyBoard.get(row).set(col, false);
            }
        }

        if (isPlayer) {
            playerShips.remove(ship);
        } else {
            enemyShips.remove(ship);
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
