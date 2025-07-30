package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.players.AI;
import java.io.Serializable;
import java.util.List;

/**
 * Represents the saved state of the game, used for serialization and persistence.

 * This class holds all the essential game data that need to be saved and restored,
 * including both players' boards, their ship placements, and the machine state.

 * It implements {@link Serializable} to allow the game state to be written to and read from
 * a file.

 * @author Juan Pablo Piedrahita Triana.
 * @version 3.2
 * @since version 3.0
 * @see Board
 * @see Ship
 * @see AI
 */
public class GameState implements Serializable {
    /**
     * The boards of both players.
     */
    private final Board playerBoard;

    private final Board enemyBoard;

    /**
     * The lists of both players' ships.
     */
    private final List<Ship> playerShips;

    private final List<Ship> enemyShips;

    /**
     * The state of the AI object.
     */
    private final AI enemy;

    /**
     * Constructs an object that holds all the necessary game data for persistence.
     *
     * @param playerBoard  the game board of the human player.
     * @param enemyBoard   the game board of the machine player.
     * @param playerShips  the list of ships placed by the player.
     * @param enemyShips   the list of ships placed by the machine player.
     * @param enemy        the AI enemy instance with its internal logic and state
     */
    public GameState(Board playerBoard, Board enemyBoard, List<Ship> playerShips, List<Ship> enemyShips, AI enemy) {
        this.playerBoard = playerBoard;
        this.enemyBoard = enemyBoard;
        this.playerShips = playerShips;
        this.enemyShips = enemyShips;
        this.enemy = enemy;
    }

    /**
     * Returns the board of the human player.
     *
     * @return the player's board.
     */
    public Board getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Returns the board of the machine AI.
     *
     * @return the machine's board.
     */
    public Board getEnemyBoard() {
        return enemyBoard;
    }

    /**
     * Returns the list of ships placed by the human player.
     *
     * @return list of the player's ships.
     */
    public List<Ship> getPlayerShips() {
        return playerShips;
    }

    /**
     * Returns the list of ships placed by the machine AI.
     *
     * @return list of the machine's ships.
     */
    public List<Ship> getEnemyShips() {
        return enemyShips;
    }

    /**
     * Returns the enemy AI instance.
     *
     * @return AI object representing the machine.
     */
    public AI getEnemy(){
        return enemy;
    }
}
