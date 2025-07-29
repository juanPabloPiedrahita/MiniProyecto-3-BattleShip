package com.example.miniproyecto3.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the game board for the Battleship game.

 * Each player has their own 10x10 board, represented as a grid of booleans.

 * This class manages ship placement, shot tracking, and sunk ship marking.

 * @author David Taborda Montenegro.

 * @version 3.2

 * @since version 1.0
 */
public class Board implements Serializable {
    /**
     * The size of the game board (10x10).
     */
    private static final int SIZE = 10;

    /**
     * 2D boolean ArrayList representing the player's and the machine's boards in the game.
     */
    private final ArrayList<ArrayList<Boolean>> playerBoard = createEmptyBoard();

    private final ArrayList<ArrayList<Boolean>> enemyBoard = createEmptyBoard();

    /**
     * Tracks where the player and the machine have fired shots in their enemy's board.
     */
    private final ArrayList<ArrayList<Boolean>> shotsOnEnemyBoard = createEmptyBoard();

    private final ArrayList<ArrayList<Boolean>> shotsOnPlayerBoard = createEmptyBoard();

    /**
     * Marks the positions where the player and the machine ships have been sunk.
     */
    private final ArrayList<ArrayList<Boolean>> sunkPlayerShips = createEmptyBoard();

    private final ArrayList<ArrayList<Boolean>> sunkEnemyShips = createEmptyBoard();

    /**
     * Creates a 10x10 board initialized with false (no ship, no shot).
     * @return A 2D ArrayList representing an empty board.
     */
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

    /**
     * Places a ship on the board for the player or machine.
     * @param row Starting row position.
     * @param col Starting column position.
     * @param size Length of the ship.
     * @param horizontal True if the ship is horizontal, false if vertical.
     * @param isPlayer True to place on player's board, false for machine.
     * @return A new Ship object representing the placed ship.
     * @throws IllegalArgumentException If the ship size is invalid.
     * @throws IndexOutOfBoundsException If the ship is out of bounds.
     * @throws IllegalStateException If the target cells are already occupied.
     * @see Ship
     */
    public Ship placeShip(int row, int col, int size, boolean horizontal, boolean isPlayer) {
        if (size <= 0) {
            throw new IllegalArgumentException("El tamaño del barco debe ser mayor a cero.");
        }

        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE ||
                (horizontal && col + size > SIZE) || (!horizontal && row + size > SIZE)) {
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

    /**
     * Removes a ship from the board and marks its cells as sunk (disappearing the ship of the board).
     * @param ship The ship to remove.
     * @param isPlayer True if removing from player's board, false for machine.
     * @see Ship
     */
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

    /**
     * For get the player's board.
     * @return The player's ship board.
     */
    public ArrayList<ArrayList<Boolean>> getPlayerBoard() {
        return playerBoard;
    }

    /**
     * For get the machine's board.
     * @return The enemy's ship board.
     */
    public ArrayList<ArrayList<Boolean>> getEnemyBoard() {
        return enemyBoard;
    }

    /**
     * For get the board where the player has shot.
     * @return The board tracking shots fired by the player.
     */
    public ArrayList<ArrayList<Boolean>> getShotsOnEnemyBoard() {
        return shotsOnEnemyBoard;
    }

    /**
     * For get the board where the machine has shot.
     * @return The board tracking shots fired by the enemy.
     */
    public ArrayList<ArrayList<Boolean>> getShotsOnPlayerBoard() {
        return shotsOnPlayerBoard;
    }

    /**
     * For get the positions where the player's ships are sunk.
     * @return The cells where player's ships have been sunk.
     */
    public ArrayList<ArrayList<Boolean>> getSunkPlayerShips() {
        return sunkPlayerShips;
    }

    /**
     * For get the positions where the machine's ships are sunk.
     * @return The cells where enemy's ships have been sunk.
     */
    public ArrayList<ArrayList<Boolean>> getSunkEnemyShips() {
        return sunkEnemyShips;
    }

    /**
     * Checks whether a cell has already been shot at.
     * @param row Row index of the shot.
     * @param col Column index of the shot.
     * @param isPlayer True if checking player's shots, false for machine's.
     * @return True if the cell has been previously shot at.
     */
    public boolean alreadyShotAt(int row, int col, boolean isPlayer) {
        return isPlayer ? shotsOnEnemyBoard.get(row).get(col) : shotsOnPlayerBoard.get(row).get(col);
    }

    /**
     * Registers a shot on the given cell.
     * @param row Row index.
     * @param col Column index.
     * @param isPlayer True if player is shooting, false if machine.
     */
    public void registerShot(int row, int col, boolean isPlayer) {
        if (isPlayer) {
            shotsOnEnemyBoard.get(row).set(col, true);
        } else {
            shotsOnPlayerBoard.get(row).set(col, true);
        }
    }

    /**
     * Checks whether a ship exists at a given cell.
     * @param row Row index.
     * @param col Column index.
     * @param isPlayer True for player's board, false for machine's.
     * @return True if a ship is present at the specified cell.
     */
    public boolean hasShipAt(int row, int col, boolean isPlayer) {
        return isPlayer ? playerBoard.get(row).get(col) : enemyBoard.get(row).get(col);
    }
}