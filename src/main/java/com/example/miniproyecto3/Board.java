package com.example.miniproyecto3;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int SIZE = 10;
    private boolean[][] playerBoard = new boolean[SIZE][SIZE]; // Indica si hay un barco en la posición
    private boolean[][] enemyBoard = new boolean[SIZE][SIZE];  // Indica si el enemigo tiene un barco en la posición
    private List<Ship> playerShips = new ArrayList<>();
    private List<Ship> enemyShips = new ArrayList<>();

    public boolean placeShip(int row, int col, int size, boolean horizontal, boolean isPlayer) {
        boolean canPlace = true;
        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            if (r >= SIZE || c >= SIZE || (isPlayer ? playerBoard[r][c] : enemyBoard[r][c])) {
                canPlace = false;
                break;
            }
        }

        if (canPlace) {
            for (int i = 0; i < size; i++) {
                int r = row + (horizontal ? 0 : i);
                int c = col + (horizontal ? i : 0);
                if (isPlayer) {
                    playerBoard[r][c] = true;
                } else {
                    enemyBoard[r][c] = true;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isHit(int row, int col, boolean isPlayer) {
        return (isPlayer ? enemyBoard[row][col] : playerBoard[row][col]);
    }

    public void resetBoard() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                playerBoard[r][c] = false;
                enemyBoard[r][c] = false;
            }
        }
        playerShips.clear();
        enemyShips.clear();
    }

    public boolean checkWin() {
        for (Ship ship : playerShips) {
            for (int i = 0; i < ship.size; i++) {
                int r = ship.startRow + (ship.horizontal ? 0 : i);
                int c = ship.startCol + (ship.horizontal ? i : 0);
                if (!playerBoard[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Método para agregar barcos a la lista de barcos
    public void addShip(Ship ship, boolean isPlayer) {
        if (isPlayer) {
            playerShips.add(ship);
        } else {
            enemyShips.add(ship);
        }
    }
}
