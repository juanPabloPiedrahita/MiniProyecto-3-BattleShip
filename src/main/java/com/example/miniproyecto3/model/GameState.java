package com.example.miniproyecto3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable{
    private Board playerBoard;
    private Board enemyBoard;
    private List<Ship> playerShips;
    private List<Ship> enemyShips;
    private boolean finishedPlacing;
    private boolean monitorMode;

    /*
    public GameState(Board playerBoard, Board enemyBoard, List<Ship> playerShips, List<Ship> enemyShips, boolean finishedPlacing, boolean monitorMode) {
        this.playerBoard = playerBoard;
        this.enemyBoard = enemyBoard;
        this.playerShips = playerShips;
        this.enemyShips = enemyShips;
        this.finishedPlacing = finishedPlacing;
        this.monitorMode = monitorMode;
    }*/

    public GameState(){

    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public Board getEnemyBoard() {
        return enemyBoard;
    }

    public void setEnemyBoard(Board enemyBoard) {
        this.enemyBoard = enemyBoard;
    }

    public List<Ship> getPlayerShips() {
        return playerShips;
    }

    public void setPlayerShips(List<Ship> playerShips) {
        this.playerShips = playerShips;
    }

    public List<Ship> getEnemyShips() {
        return enemyShips;
    }

    public void setEnemyShips(List<Ship> enemyShips) {
        this.enemyShips = enemyShips;
    }

    public boolean isFinishedPlacing() {
        return finishedPlacing;
    }

    public void setFinishedPlacing(boolean finishedPlacing) {
        this.finishedPlacing = finishedPlacing;
    }

    public boolean isMonitorMode() {
        return monitorMode;
    }

    public void setMonitorMode(boolean monitorMode) {
        this.monitorMode = monitorMode;
    }


}
