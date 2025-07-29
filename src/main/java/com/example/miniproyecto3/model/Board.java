package com.example.miniproyecto3.model;

import java.io.Serializable;
import java.util.ArrayList;


/**

 Represents the game board for the Battleship game.

 <p>
 Each player has their own 10x10 board, represented as a grid of booleans.

 This class manages ship placement, shot tracking, and sunk ship marking.

 </p>
 @author David Taborda Montenegro

 @version 3.2

 @since 2025-05-15
 */
public class Board implements Serializable {
    /**

     The fixed size of the game board (10x10).
     */
    private static final int SIZE = 10;

    /**

     2D boolean matrix representing the player's ship positions.

     {@code true} if a ship occupies the cell.
     */
    private final ArrayList<ArrayList<Boolean>> playerBoard = createEmptyBoard();

    /**

     2D boolean matrix representing the enemy's ship positions.

     {@code true} if a ship occupies the cell.
     */
    private final ArrayList<ArrayList<Boolean>> enemyBoard = createEmptyBoard();

    /**

     Tracks where the player has fired shots at the enemy board.

     {@code true} means a shot was fired at that cell.
     */
    private final ArrayList<ArrayList<Boolean>> shotsOnEnemyBoard = createEmptyBoard();

    /**

     Tracks where the enemy has fired shots at the player's board.

     {@code true} means a shot was fired at that cell.
     */
    private final ArrayList<ArrayList<Boolean>> shotsOnPlayerBoard = createEmptyBoard();

    /**

     Marks the positions where the player's ships have been sunk.

     {@code true} indicates a ship was sunk at that cell.
     */
    private final ArrayList<ArrayList<Boolean>> sunkPlayerShips = createEmptyBoard();

    /**

     Marks the positions where the enemy's ships have been sunk.

     {@code true} indicates a ship was sunk at that cell.
     */
    private final ArrayList<ArrayList<Boolean>> sunkEnemyShips = createEmptyBoard();

    /**

     Creates a 10x10 board initialized with false (no ship, no shot).

     @return A 2D boolean list representing an empty board.
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

     Places a ship on the board for the player or enemy.

     @param row Starting row position.

     @param col Starting column position.

     @param size Length of the ship.

     @param horizontal True if the ship is horizontal, false if vertical.

     @param isPlayer True to place on player's board, false for enemy.

     @return A new Ship object representing the placed ship.

     @throws IllegalArgumentException If the ship size is invalid.

     @throws IndexOutOfBoundsException If the ship would go out of bounds.

     @throws IllegalStateException If the target cells are already occupied.

     @see Ship
     */
    public Ship placeShip(int row, int col, int size, boolean horizontal, boolean isPlayer) {
        if (size <= 0) {
            throw new IllegalArgumentException("Ship size must be greater than zero.");
        }

        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE ||
                (horizontal && col + size > SIZE) || (!horizontal && row + size > SIZE)) {
            throw new IndexOutOfBoundsException("Ship goes out of board bounds.");
        }

        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            boolean occupied = isPlayer ? playerBoard.get(r).get(c) : enemyBoard.get(r).get(c);
            if (occupied) {
                throw new IllegalStateException("Ship already present at specified location.");
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

     Removes a ship from the board and marks its cells as sunk.

     @param ship The ship to remove.

     @param isPlayer True if removing from player's board, false for enemy.

     @see Ship
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

     @return The player's ship board.
     */
    public ArrayList<ArrayList<Boolean>> getPlayerBoard() {
        return playerBoard;
    }

    /**

     @return The enemy's ship board.
     */
    public ArrayList<ArrayList<Boolean>> getEnemyBoard() {
        return enemyBoard;
    }

    /**

     @return The board tracking shots fired by the player.
     */
    public ArrayList<ArrayList<Boolean>> getShotsOnEnemyBoard() {
        return shotsOnEnemyBoard;
    }

    /**

     @return The board tracking shots fired by the enemy.
     */
    public ArrayList<ArrayList<Boolean>> getShotsOnPlayerBoard() {
        return shotsOnPlayerBoard;
    }

    /**

     @return The cells where player's ships have been sunk.
     */
    public ArrayList<ArrayList<Boolean>> getSunkPlayerShips() {
        return sunkPlayerShips;
    }

    /**

     @return The cells where enemy's ships have been sunk.
     */
    public ArrayList<ArrayList<Boolean>> getSunkEnemyShips() {
        return sunkEnemyShips;
    }

    /**

     Checks whether a cell has already been shot at.

     @param row Row index of the shot.

     @param col Column index of the shot.

     @param isPlayer True if checking player's shots, false for enemy's.

     @return True if the cell has been previously shot at.
     */
    public boolean alreadyShotAt(int row, int col, boolean isPlayer) {
        return isPlayer ? shotsOnEnemyBoard.get(row).get(col) : shotsOnPlayerBoard.get(row).get(col);
    }

    /**

     Registers a shot on the given cell.

     @param row Row index.

     @param col Column index.

     @param isPlayer True if player is shooting, false if enemy.
     */
    public void registerShot(int row, int col, boolean isPlayer) {
        if (isPlayer) {
            shotsOnEnemyBoard.get(row).set(col, true);
        } else {
            shotsOnPlayerBoard.get(row).set(col, true);
        }
    }

    /**

     Checks whether a ship exists at a given cell.

     @param row Row index.

     @param col Column index.

     @param isPlayer True for player's board, false for enemy's.

     @return True if a ship is present at the specified cell.
     */
    public boolean hasShipAt(int row, int col, boolean isPlayer) {
        return isPlayer ? playerBoard.get(row).get(col) : enemyBoard.get(row).get(col);
    }
}