package com.example.miniproyecto3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a ship in the game.

 * A ship has a size, a starting position (row and column) and an orientation (horizontal and vertical).

 * @author David Taborda Montenegro.

 * @version 3.0

 * @since version 1.0
 */
public class Ship implements Serializable {
    /**
     * The number of cells the ship occupies on the board.
     */
    private final int size;

    /**
     * The starting row of the ship on the board.
     */
    private final int startRow;

    /**
     * The starting column of the ship on the board.
     */
    private final int startCol;

    /**
     * Indicates whether the ship is placed horizontally or vertically.
     */
    private final boolean horizontal;

    /**
     * List of booleans representing hits on each segment of the ship.
     */
    private final List<Boolean> hits;

    /**
     * Constructs a new Ship instance.
     * @param size The number of cells the ship occupies.
     * @param startRow Starting row of the ship on the board.
     * @param startCol Starting column of the ship on the board.
     * @param horizontal True if the ship is horizontal, false if vertical.
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
     * Checks whether the ship is completely sunk.
     * @return True if all segments have been hit.
     */
    public boolean isSunk() {
        for (boolean hit : hits) {
            if (!hit) return false;
        }
        return true;
    }

    /**
     * Registers a hit on the ship if the given coordinates match a segment.
     * @param row Row of the impact.
     * @param col Column of the impact.
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
     * Returns a list of coordinates occupied by the ship on the board.
     * @return List of {@link Coordinate} objects, representing the ship's position.
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
     * Checks whether the ship occupies a specific coordinate.
     * @param row Row to check.
     * @param col Column to check.
     * @return True if the ship occupies the given coordinate.
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
     * To get the size of the ship.
     * @return The size of the ship.
     */
    public int getSize() {
        return size;
    }

    /**
     * To get the orientation of the ship.
     * @return True if the ship is placed horizontally, false if is placed vertically.
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * Inner static class that represents a coordinate on the board.
     * Used for storing the ship's occupied positions.
     */
    public static class Coordinate implements Serializable {
        /**
         * Row index of the coordinate.
         */
        private final int row;

        /**
         * Column index of the coordinate.
         */
        private final int col;

        /**
         * Constructs a coordinate with specified row and column.
         * @param row The row index.
         * @param col The column index.
         */
        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }

        /**
         * To get the row where the ship is placed.
         * @return The row index.
         */
        public int getRow() {
            return row;
        }

        /**
         * To get the column where the ship is placed.
         * @return The column index.
         */
        public int getCol() {
            return col;
        }
    }
}