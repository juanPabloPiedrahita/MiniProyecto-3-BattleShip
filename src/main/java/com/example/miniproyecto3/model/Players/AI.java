package com.example.miniproyecto3.model.Players;

import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;

import java.io.Serializable;
import java.util.*;

public class AI extends PlayerAdapter implements Serializable{

    private final List<Ship.Coordinate> pendingTargets = new ArrayList<>();
    private final Random rand = new Random();
    private int score;
    private String name;
    private String dificulty;
    private int lastShotRow;
    private int lastShotCol;

    public AI(int score,String name,String dificulty) {
        this.score = score;
        this.name = name;
        this.dificulty = dificulty;
    }

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
                        opponentBoardModel.removeShip(ship, false);
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
                        opponentBoardModel.removeShip(ship, false);
                    }
                    break;
                }
            }
        }

        lastShotRow = row;
        lastShotCol = col;

        return hit;
    }

    public int getLastShotRow() {return lastShotRow;}

    public int getLastShotCol() {return lastShotCol;}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    public int getDificulty(){
        return(dificulty.equals("Fácil") ? 1 : 2);
    }

    public void setDificulty(String dificulty){
        this.dificulty = dificulty;
    }
}
