package com.example.miniproyecto3;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int SIZE = 10;
    private boolean[][] playerBoard = new boolean[SIZE][SIZE]; // Indica si hay un barco en la posición
    private boolean[][] enemyBoard = new boolean[SIZE][SIZE];  // Indica si el enemigo tiene un barco en la posición
    private boolean[][] shotsOnEnemyBoard = new boolean[SIZE][SIZE];
    private boolean[][] shotsOnPlayerBoard = new boolean[SIZE][SIZE];
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
            Ship ship = new Ship(size, row, col, horizontal);
            if(isPlayer) {
                playerShips.add(ship);
            } else {
                enemyShips.add(ship);
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
                shotsOnEnemyBoard[r][c] = false;
                shotsOnPlayerBoard[r][c] = false;
            }
        }
        playerShips.clear();
        enemyShips.clear();
    }

    public boolean checkWin() {
        // Cambié la lógica para chequear si todos los barcos enemigos han sido hundidos
        for (Ship ship : enemyShips) {
            for (int i = 0; i < ship.size; i++) {
                int r = ship.startRow + (ship.horizontal ? 0 : i);
                int c = ship.startCol + (ship.horizontal ? i : 0);
                if (enemyBoard[r][c]) {
                    return false;  // Si aún hay una parte del barco que no ha sido destruido
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

    // Método que obtiene las coordenadas de un barco
    public List<int[]> getShipCoordinates(Ship ship) {
        List<int[]> coordinates = new ArrayList<>();
        for (int i = 0; i < ship.size; i++) {
            int r = ship.startRow + (ship.horizontal ? 0 : i);
            int c = ship.startCol + (ship.horizontal ? i : 0);
            coordinates.add(new int[]{r, c});
        }
        return coordinates;
    }

    public Ship shoot(int row, int col, boolean isPlayer) {
        if(alreadyShotAt(row, col, isPlayer)) return null;

        registerShot(row, col, isPlayer);

        List<Ship> targetShips = isPlayer ? enemyShips : playerShips;
        boolean[][] targetBoard = isPlayer ? enemyBoard : playerBoard;

        if(targetBoard[row][col]) {
            for(Ship ship : targetShips) {
                if(ship.occupies(row, col)) {
                    ship.registerHit(row, col);
                    return ship;
                    //return null;
                }
                /*if(ship.isSunk()) {
                    System.out.println("Barco hundido en " + row + ", " + col + ".");
                    return ship;
                */
            }
        }
        return null;
        /*boolean hit = enemyBoard[row][col];

        if(hit) {
            for(Ship ship : enemyShips) {
                if(ship.registerHit(row, col)) {
                    if(ship.isSunk()) {
                        System.out.println("Barco hundido en " + row + ", " + col + ".");
                        Ship sunkShipLast = ship;
                    }
                    break;
                }
            }
            return true;
        }

        return false;*/
        /*if(hit){
            //enemyBoard[row][col] = false;
            return true;
        }
        return false;*/
    }

    public boolean wasShot(int row, int col){
        return shotsOnEnemyBoard[row][col];
    }

    public boolean wasHit(int row, int col) {
        for(Ship ship : enemyShips) {
            if(ship.occupies(row, col)) {
                return shotsOnEnemyBoard[row][col];
            }
        }
        return false;
    }

    public boolean[][] getPlayerBoard() {
        return playerBoard;
    }

    public boolean[][] getEnemyBoard() {
        return enemyBoard;
    }

    public List<Ship> getEnemyShips() {
        return enemyShips;
    }

    public boolean alreadyShotAt(int row, int col, boolean isPlayer) {
        return isPlayer ? shotsOnEnemyBoard[row][col] : shotsOnPlayerBoard[row][col];
    }

    public void registerShot(int row, int col, boolean isPlayer) {
        if(isPlayer) {
            shotsOnEnemyBoard[row][col] = true;
        } else {
            shotsOnPlayerBoard[row][col] = true;
        }
    }

    public boolean hasShipAt(int row, int col, boolean isPlayer) {
        return isPlayer ? playerBoard[row][col] : enemyBoard[row][col];
    }

}