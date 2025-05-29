package com.example.miniproyecto3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa un barco en el juego de Batalla Naval.
 * Cada barco tiene un tamaño, una posición inicial (fila y columna),
 * una orientación (horizontal o vertical) y una lista de impactos.
 */
public class Ship implements Serializable {
    private int size;
    private int startRow;
    private int startCol;
    private boolean horizontal;
    private List<Boolean> hits; // Lista que guarda si cada parte del barco fue impactada

    /**
     * Constructor del barco.
     *
     * @param size       Tamaño del barco (número de casillas que ocupa)
     * @param startRow   Fila inicial donde comienza el barco
     * @param startCol   Columna inicial donde comienza el barco
     * @param horizontal true si el barco está en orientación horizontal
     */
    public Ship(int size, int startRow, int startCol, boolean horizontal) {
        this.size = size;
        this.startRow = startRow;
        this.startCol = startCol;
        this.horizontal = horizontal;
        this.hits = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            hits.add(false); // Inicialmente, ninguna parte del barco ha sido impactada
        }
    }

    /**
     * Verifica si el barco ha sido hundido (todas sus partes han sido impactadas).
     *
     * @return true si todas las posiciones fueron impactadas
     */
    public boolean isSunk() {
        for (boolean hit : hits) {
            if (!hit) return false;
        }
        return true;
    }

    /**
     * Registra un impacto en el barco si la coordenada coincide con alguna parte del barco.
     *
     * @param row Fila del impacto
     * @param col Columna del impacto
     */
    public void registerHit(int row, int col) {
        for (int i = 0; i < size; i++) {
            int r = startRow + (horizontal ? 0 : i);
            int c = startCol + (horizontal ? i : 0);
            if (r == row && c == col) {
                hits.set(i, true); // Marca esa parte como impactada
                break;
            }
        }
    }

    /**
     * Devuelve una lista de coordenadas que ocupa el barco en el tablero.
     *
     * @return Lista de objetos Coordinate con las posiciones ocupadas
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
     * Verifica si el barco ocupa una coordenada específica.
     *
     * @param row Fila a verificar
     * @param col Columna a verificar
     * @return true si el barco ocupa esa coordenada
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

    // Getters públicos para acceder a los atributos del barco

    public int getSize() {
        return size;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * Clase interna que representa una coordenada en el tablero.
     * Es estática porque no necesita acceder a los atributos de la clase externa Ship.
     */
    public static class Coordinate {
        private final int row;
        private final int col;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {return row;}
        public int getCol() {return col;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Coordinate)) return false;
            Coordinate that = (Coordinate) o;
            return row == that.row && col == that.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
}

