package com.example.miniproyecto3.model.Players;

import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;

import java.util.List;

public abstract class PlayerAdapter implements IPlayer{
    @Override
    public boolean makeMove(int row, int col, Board opponentBoardModel, List<Ship> opponentShips) {
        return false;
    }

    @Override
    public String getName() {
        return "";
    }

}
