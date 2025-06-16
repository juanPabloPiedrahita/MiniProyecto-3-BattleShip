package com.example.miniproyecto3.model.Players;

import com.example.miniproyecto3.controller.GameController;
import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;
import javafx.scene.layout.GridPane;

import java.util.List;

public abstract class PlayerAdapter implements IPlayer{
    @Override
    public void makeMove(int row, int col, Board ownBoard, Board opponentBoardModel, GridPane opponentGrid, Runnable onTurnEnd, List<Ship> playerShips, GameController gameController) {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public void setScore(int score) {

    }

    @Override
    public void setName(String name) {

    }
}
