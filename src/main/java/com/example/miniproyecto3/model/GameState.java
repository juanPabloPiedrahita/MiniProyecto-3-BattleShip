package com.example.miniproyecto3.model;

import java.io.Serializable;
import java.util.List;

//Esta clase esta hecha para guardar el estado del juego (listas de barcos (jugador y enemigo) y tablero de ambos. Esta implementa la serializacion para poder ser guardada en un archivo .ser
public class GameState implements Serializable {
    private Board playerBoard;
    private Board enemyBoard;
    private List<Ship> playerShips;
    private List<Ship> enemyShips;
    public GameState(Board playerBoard, Board enemyBoard, List<Ship> playerShips, List<Ship> enemyShips) {
        this.playerBoard = playerBoard;
        this.enemyBoard = enemyBoard;
        this.playerShips = playerShips;
        this.enemyShips = enemyShips;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public Board getEnemyBoard() {
        return enemyBoard;
    }

    public List<Ship> getPlayerShips() {
        return playerShips;
    }

    public List<Ship> getEnemyShips() {
        return enemyShips;
    }

    public void setEnemyBoard(Board board) {
        this.enemyBoard = board;
    }

    public void setPlayerBoard(Board board) {
        this.playerBoard = board;
    }
}
