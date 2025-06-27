package com.example.miniproyecto3.model.Players;

import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;

import java.util.List;

public interface IPlayer {

    //Esta doc la hice con JavaDoc solo para probar ;v
    /**
     * Ejecuta un disparo contra el oponente.
     *
     * @param row Representa la fila a donde disparar.
     * @param col Representa la columna a donde impactar.
     * @param opponentBoardModel El modelo del tablero oponente
     * @param opponentShips La lista de las embarcaciones del enemigo correspondiente.
     */

    boolean makeMove(int row, int col, Board opponentBoardModel, List<Ship> opponentShips);

    String getName();

    int getScore();

    void setScore(int score);

    void setName(String name);
}
