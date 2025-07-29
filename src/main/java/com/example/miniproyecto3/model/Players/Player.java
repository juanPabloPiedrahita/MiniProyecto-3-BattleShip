package com.example.miniproyecto3.model.Players;

import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;
import com.example.miniproyecto3.exceptions.DoubleShootException;

import java.io.Serializable;
import java.util.List;

public class Player extends PlayerAdapter implements Serializable {
    private final String playerName;
    private int playerScore;

    public Player(String playerName, int playerScore) {
        this.playerName = playerName;
        this.playerScore = playerScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

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
