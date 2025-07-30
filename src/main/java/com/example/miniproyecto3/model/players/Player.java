package com.example.miniproyecto3.model.players;

import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;
import com.example.miniproyecto3.exceptions.DoubleShootException;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a human player in the game.

 * This class extends {@link PlayerAdapter} and implements actual game logic for making moves.
 * Each player has a name and a score that increases when enemy ships are sunk.

 * This class is also {@link Serializable}, allowing the player's state to be persisted.

 * @author David Taborda Montenegro
 * @version 3.0
 * @since version 3.0
 * @see IPlayer
 * @see PlayerAdapter
 * @see com.example.miniproyecto3.exceptions.DoubleShootException
 */
public class Player extends PlayerAdapter implements Serializable {
    /**
     * The name and the score of the human player.
     */
    private final String playerName;

    private int playerScore;

    /**
     * Constructs a new player with the specified name and initial score.
     *
     * @param playerName the name of the player.
     * @param playerScore the initial score of the player.
     */
    public Player(String playerName, int playerScore) {
        this.playerName = playerName;
        this.playerScore = playerScore;
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the current score of the player.
     *
     * @return the player's score.
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * Executes a move (attack) on the opponent's board.
     * The method checks whether the cell has already been targeted. If so, it throws a
     * {@link DoubleShootException}. Otherwise, it registers the shot, checks for hits,
     * applies damage to the appropriate ship, and updates the player's score if a ship is sunk.
     *
     * @param row the row coordinate of the move.
     * @param col the column coordinate of the move.
     * @param opponentBoardModel the logic board model of the opponent.
     * @param opponentShips the list of the opponent's ships.
     * @return True if the shot hits a ship or false otherwise.
     * @throws DoubleShootException if the player tries to shoot a cell already targeted.
     */
    @Override
    public boolean makeMove(int row, int col, Board opponentBoardModel, List<Ship> opponentShips) {
        if(opponentBoardModel.alreadyShotAt(row,col,true)){
            throw new DoubleShootException("Ya disparaste en la casilla (" + row + "," + col + ").");
        }

        opponentBoardModel.registerShot(row, col, true);
        boolean hit = opponentBoardModel.hasShipAt(row, col, false);

        if(hit) {
            for(Ship ship : opponentShips) {
                if(ship.occupies(row, col)) {
                    ship.registerHit(row, col);
                    if(ship.isSunk()) {
                        playerScore += 1;
                        opponentBoardModel.removeShip(ship, false);
                    }
                    break;
                }
            }
        }
        return hit;
    }
}
