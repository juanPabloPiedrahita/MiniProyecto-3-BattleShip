package com.example.miniproyecto3.model.Players;

import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;
import java.util.List;

/**
 * Represents a generic player in the game.

 * This interface defines the basic contract that all player types
 * must implement, including making a move and providing a player name.

 * @author Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 3.0
 * @see Player
 * @see AI
 */
public interface IPlayer {

    /**
     * Executes a move (attack) against the opponent by selecting a target coordinate.
     *
     * @param row the row coordinate of the target cell.
     * @param col the column coordinate of the target cell.
     * @param opponentBoardModel the board model of the opponent (to register hits/misses).
     * @param opponentShips the list of ships placed by the opponent (to check for hits).
     * @return True if the move results in a hit or false otherwise.
     */
    boolean makeMove(int row, int col, Board opponentBoardModel, List<Ship> opponentShips);

    /**
     * Returns the name or identifier of the player.
     *
     * @return the name of the player.
     */
    String getName();
}
