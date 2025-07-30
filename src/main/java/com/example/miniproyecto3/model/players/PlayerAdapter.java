package com.example.miniproyecto3.model.players;

import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;
import java.util.List;

/**
 * An abstract adapter class for the {@link IPlayer} interface.

 * This class provides default implementations of all methods in IPlayer,
 * allowing subclasses to override only the methods they need.

 * It follows the adapter design pattern to reduce boilerplate code in player classes.

 * @author Juan Pablo Piedrahita Triana.
 * @version 3.0
 * @since version 3.0
 * @see IPlayer
 */
public abstract class PlayerAdapter implements IPlayer{

    /**
     * Default implementation of the method makeMove, which does nothing and returns the default value of false.
     * Subclasses should override this method with actual game logic.
     *
     * @param row the row coordinate of the move.
     * @param col the column coordinate of the move.
     * @param opponentBoardModel the opponent's logic board model.
     * @param opponentShips the list of the opponent's ships.
     * @return False by default.
     */
    @Override
    public boolean makeMove(int row, int col, Board opponentBoardModel, List<Ship> opponentShips) {
        return false;
    }

    /**
     * Default implementation of the method getName, which returns an empty string.
     * Subclasses should override this method to return the actual player name.
     *
     * @return an empty string by default
     */
    @Override
    public String getName() {
        return "";
    }
}
