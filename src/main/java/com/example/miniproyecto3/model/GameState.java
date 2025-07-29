package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.Players.AI;
import java.io.Serializable;
import java.util.List;

//Esta clase esta hecha para guardar el estado del juego (listas de barcos (jugador y enemigo) y tablero de ambos. Esta implementa la serializacion para poder ser guardada en un archivo .ser
public class GameState implements Serializable {
    private final Board playerBoard;
    private final Board enemyBoard;
    private final List<Ship> playerShips;
    private final List<Ship> enemyShips;
    private final AI enemy;

    public GameState(Board playerBoard, Board enemyBoard, List<Ship> playerShips, List<Ship> enemyShips, AI enemy) {
        this.playerBoard = playerBoard;
        this.enemyBoard = enemyBoard;
        this.playerShips = playerShips;
        this.enemyShips = enemyShips;
        this.enemy = enemy;
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
