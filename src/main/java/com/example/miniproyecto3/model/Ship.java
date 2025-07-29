package com.example.miniproyecto3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**

 Represents a ship in the Battleship game.

 A ship has a size, a starting position (row and column),

 an orientation (horizontal or vertical), and tracks its hits.

 @author David Taborda Montenegro

 @version 3.0

 @since 2025-05-20
 */
public class Ship implements Serializable {

    /**

     The number of cells the ship occupies on the board.
     */
    private final int size;

    /**

     The starting row of the ship on the board.
     */
    private final int startRow;

    /**

     The starting column of the ship on the board.
     */
    private final int startCol;

    /**

     Indicates whether the ship is placed horizontally.
     */
    private final boolean horizontal;

    /**

     List of booleans representing hits on each segment of the ship.

     {@code true} if the corresponding segment has been hit.
     */
    private final List<Boolean> hits;

    /**

     Constructs a new Ship instance.

     @param size Number of cells the ship occupies.

     @param startRow Starting row on the board.

     @param startCol Starting column on the board.

     @param horizontal {@code true} if the ship is horizontal, {@code false} if vertical.
     */
    public Ship(int size, int startRow, int startCol, boolean horizontal) {
        this.size = size;
        this.startRow = startRow;
        this.startCol = startCol;
        this.horizontal = horizontal;
        this.hits = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            hits.add(false);
        }
    }

    /**

     Checks whether the ship is completely sunk.

     @return {@code true} if all segments have been hit.
     */
    public boolean isSunk() {
        for (boolean hit : hits) {
            if (!hit) return false;
        }
        return true;
    }

    /**

     Registers a hit on the ship if the given coordinates match a segment.

     @param row Row of the impact.

     @param col Column of the impact.
     */
    public void registerHit(int row, int col) {
        for (int i = 0; i < size; i++) {
            int r = startRow + (horizontal ? 0 : i);
            int c = startCol + (horizontal ? i : 0);
            if (r == row && c == col) {
                hits.set(i, true);
                break;
            }
        }
    }

    /**

     Returns a list of coordinates occupied by the ship on the board.

     @return List of {@link Coordinate} objects representing the ship's position.
     */
    public List<Coordinate> getCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int r = startRow + (horizontal ? 0 : i);
            int c = startCol + (horizontal ? i : 0);
            coordinates.add(new Coordinate(r, c));
        }
        return coordinates;
    }

    /**

     Checks whether the ship occupies a specific coordinate.

     @param row Row to check.

     @param col Column to check.

     @return {@code true} if the ship occupies the given coordinate.
     */
    public boolean occupies(int row, int col) {
        for (int i = 0; i < size; i++) {
            int r = startRow + (horizontal ? 0 : i);
            int c = startCol + (horizontal ? i : 0);
            if (r == row && c == col) {
                return true;
            }
        }
        return false;
    }

    /**

     @return The size of the ship.
     */
    public int getSize() {
        return size;
    }

    /**

     @return {@code true} if the ship is placed horizontally.
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**

     Represents a coordinate on the board.

     Used for storing the ship's occupied positions.
     */
    public static class Coordinate implements Serializable {

        /**

         Row index of the coordinate.
         */
        private final int row;

        /**

         Column index of the coordinate.
         */
        private final int col;

        /**

         Constructs a coordinate with specified row and column.

         @param row The row index.

         @param col The column index.
         */
        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }

        /**

         @return The row index.
         */
        public int getRow() {
            return row;
        }

        /**

         @return The column index.
         */
        public int getCol() {
            return col;
        }
    }
}