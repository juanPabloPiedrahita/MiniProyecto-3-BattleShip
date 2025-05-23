package com.example.miniproyecto3.model.Players;

import com.example.miniproyecto3.controller.GameController;
import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;
import javafx.scene.layout.*;

import java.util.List;

public interface IPlayer {

    //Esta doc la hice con JavaDoc solo para probar ;v
    /**
     * Ejecuta un disparo contra el oponente.
     * @param ownBoard   El tablero de este jugador (para registrar impactos)
     * @param opponentBoardModel  El modelo del tablero oponente
     * @param opponentGrid  El GridPane donde se muestran los disparos en la UI
     * @param onTurnEnd  Un runnable que se ejecuta al terminar el turno (por fallo)
     */

    void makeMove(int row, int col, Board ownBoard, Board opponentBoardModel, GridPane opponentGrid, Runnable onTurnEnd, List<Ship> playerShips, GameController gameController);

    String getName();

    int getScore();

    void setScore(int score);

    void setName(String name);
}
