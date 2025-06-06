package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.Players.AI;
import java.io.Serializable;
import java.util.List;

//Esta clase esta hecha para guardar el estado del juego (listas de barcos (jugador y enemigo) y tablero de ambos. Esta implementa la serializacion para poder ser guardada en un archivo .ser
public class GameState implements Serializable {
    private Board playerBoard;
    private Board enemyBoard;
    private List<Ship> playerShips;
    private List<Ship> enemyShips;
    private AI enemy;

    public GameState(Board playerBoard, Board enemyBoard, List<Ship> playerShips, List<Ship> enemyShips, AI enemy) {
        this.playerBoard = playerBoard;
        this.enemyBoard = enemyBoard;
        this.playerShips = playerShips;
        this.enemyShips = enemyShips;
        this.enemy = enemy;
    }

    //construtor sobrecargado para la preparacion del juego
    public GameState(Board playerBoard, List<Ship> playerShips) {
        this.playerBoard = playerBoard;
        this.playerShips = playerShips;
        enemyBoard = null;
        enemyShips = null;
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

    public AI getEnemy(){
        return enemy;
    }
}
