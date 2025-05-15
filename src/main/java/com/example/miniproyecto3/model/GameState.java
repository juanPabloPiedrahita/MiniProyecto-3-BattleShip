package com.example.miniproyecto3.model;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private Board playerBoard;
    private Board enemyBoard;
    private List<Ship> playerShips;
    private List<Ship> enemyShips;
    private String playerName;

    public GameState(Board playerBoard, Board enemyBoard, List<Ship> playerShips, List<Ship> enemyShips, String playerName) {
        this.playerBoard = playerBoard;
        this.enemyBoard = enemyBoard;
        this.playerShips = playerShips;
        this.enemyShips = enemyShips;
        this.playerName = playerName;
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

    public String getPlayerName() {
        return playerName;
    }
}
