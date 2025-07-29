package com.example.miniproyecto3.model.Players;

import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;
import java.io.Serializable;
import java.util.*;

/**
 * Represents the computer-controlled opponent in the game.

 * The AI uses basic logic to randomly select cells and prioritize adjacent cells when a ship is hit,
 * simulating a slightly smarter behavior based on difficulty.

 * The AI maintains a List of pending target coordinates to simulate strategy,
 * and can adjust its difficulty level to affect behavior.

 * This class is {@link Serializable} to support saving and restoring game state.

 * @author David Taborda Montenegro.
 * @version 3.0
 * @since version 3.0
 * @see IPlayer
 * @see PlayerAdapter
 */
public class AI extends PlayerAdapter implements Serializable{

    /**
     * List that save the pending targets for the AI behavior.
     */
    private final List<Ship.Coordinate> pendingTargets = new ArrayList<>();

    /**
     * The attribute that allows select a random position to attack, in the player's board.
     */
    private final Random rand = new Random();

    /**
     * Attribute for save the machine's score (for next versions).
     */
    private final int score;

    /**
     * Save the name of the machine in the game.
     */
    private final String name;

    /**
     * Save the difficulty that the machine will have, affecting its behavior.
     */
    private String difficulty;

    /**
     * Indicates the last row where the machine shot, for the AI behavior.
     */
    private int lastShotRow;

    /**
     * Indicates the last column where the machine shot, for the AI behavior.
     */
    private int lastShotCol;

    /**
     * Constructs a new AI player with initial score, name, and difficulty level.
     *
     * @param score the starting score for the machine.
     * @param name the name of the machine player.
     * @param difficulty the difficulty level that the machine will have.
     */
    public AI(int score,String name,String difficulty) {
        this.score = score;
        this.name = name;
        this.difficulty = difficulty;
    }

    /**
     * Executes a move using the AI's decision-making strategy.
     * The AI will select a pending adjacent target (if available), or choose a random untargeted cell.
     * If a hit is successful, adjacent cells are listed as new targets. If a ship is sunk, the list is cleared.
     *
     * @param row1 ignored (not used by AI).
     * @param col1 ignored (not used by AI).
     * @param opponentBoardModel the opponent's logic board model.
     * @param opponentShips the opponent's ships.
     * @return True if the move hits a ship or false otherwise.
     */
    @Override
    public boolean makeMove(int row1, int col1, Board opponentBoardModel,
                         List<Ship> opponentShips) {

        int row, col;

        if (!pendingTargets.isEmpty()) {
            Ship.Coordinate coordinate = pendingTargets.remove(0);
            row = coordinate.getRow();
            col = coordinate.getCol();
        } else {
            do {
                row = rand.nextInt(10);
                col = rand.nextInt(10);
            } while (opponentBoardModel.alreadyShotAt(row, col, false));
        }

        opponentBoardModel.registerShot(row, col, false);

        boolean hit = opponentBoardModel.hasShipAt(row, col, true);

        if (hit) {
            for(Ship ship : opponentShips) {
                if(ship.occupies(row, col)) {
                    ship.registerHit(row, col);
                    if(ship.isSunk()) {
                        opponentBoardModel.removeShip(ship, true);
                        pendingTargets.clear();
                    } else {
                        addAdjacentTargets(row, col, opponentBoardModel);
                    }
                }
            }
        }

        lastShotRow = row;
        lastShotCol = col;

        return hit;
    }


    /**
     * Adds adjacent coordinates to the list of pending targets for future moves.
     * Only valid and untargeted cells are added.
     *
     * @param row the row of the last hit.
     * @param col the column of the last hit.
     * @param opponentBoard the opponent's board to verify valid positions.
     * @see Ship.Coordinate
     */
    private void addAdjacentTargets(int row, int col, Board opponentBoard) {
        List<Ship.Coordinate> directions = List.of(
          new Ship.Coordinate(-1, 0),
          new Ship.Coordinate(1, 0),
          new Ship.Coordinate(0, -1),
          new Ship.Coordinate(0, 1)
        );

        for(Ship.Coordinate dir : directions){
            int r = row + dir.getRow();
            int c = col + dir.getCol();

            if(r >= 0 && r < 10 && c >= 0 && c < 10 && !opponentBoard.alreadyShotAt(r, c, false)){
                pendingTargets.add(new Ship.Coordinate(r, c));
            }
        }
    }

    /**
     * Performs a purely random move, independent of strategic targeting.
     *
     * @param opponentBoardModel the opponent's logic board.
     * @param opponentShips the list of opponent ships.
     * @see Ship
     * @return True if the move hits a ship, or false otherwise.
     */
    public boolean makeRandomMove(Board opponentBoardModel, List<Ship> opponentShips) {
        int row, col;
        do {
            row = rand.nextInt(10);
            col = rand.nextInt(10);
        } while (opponentBoardModel.alreadyShotAt(row, col, false));

        opponentBoardModel.registerShot(row, col, false);
        boolean hit = opponentBoardModel.hasShipAt(row, col, true);

        if(hit) {
            for(Ship ship : opponentShips) {
                if(ship.occupies(row, col)) {
                    ship.registerHit(row, col);
                    if(ship.isSunk()) {
                        opponentBoardModel.removeShip(ship, true);
                    }
                    break;
                }
            }
        }

        lastShotRow = row;
        lastShotCol = col;

        return hit;
    }

    /**
     * Returns the row index of the last shot made by the AI.
     *
     * @return the row of the last shot.
     */
    public int getLastShotRow() {return lastShotRow;}

    /**
     * Returns the column index of the last shot made by the AI.
     *
     * @return the column of the last shot.
     */
    public int getLastShotCol() {return lastShotCol;}

    /**
     * Returns the name of the machine player.
     *
     * @return the machine's name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the numeric difficulty level of the AI.
     *
     * @return 1 for "Fácil", 2 for "Difícil".
     */
    public int getDifficulty(){
        return(difficulty.equals("Fácil") ? 1 : 2);
    }

    /**
     * Sets the difficulty level of the AI.
     *
     * @param difficulty the difficulty level.
     */
    public void setDifficulty(String difficulty){
        this.difficulty = difficulty;
    }
}
