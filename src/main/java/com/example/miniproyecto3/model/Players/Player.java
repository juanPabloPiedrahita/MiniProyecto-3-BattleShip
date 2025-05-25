package com.example.miniproyecto3.model.Players;

import com.example.miniproyecto3.controller.GameController;
import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.List;

public class Player extends IPlayerAdapter {
    private String playerName;
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

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    @Override
    public void makeMove(int row, int col, Board ownBoard, Board opponentBoardModel, GridPane opponentGrid, Runnable onTurnEnd, List<Ship> playerShips, GameController gameController){
        if(ownBoard.alreadyShotAt(row,col,true)){
            return;
        }

        StackPane cell = gameController.getStackPaneAt(opponentGrid,row,col);
        ownBoard.registerShot(row,col,true);
        boolean hit = ownBoard.hasShipAt(row,col,false);

        Canvas canvas = new Canvas(30,30);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setMouseTransparent(true);
        canvas.setManaged(false);
        canvas.setUserData(hit ? "impacto" : "fallo");
        gameController.drawShot(gc,hit,false);
        cell.getChildren().add(canvas);
        if(hit){
            Ship targetShip = gameController.getShipAt(playerShips,row,col);
            targetShip.registerHit(row,col);
            if(targetShip.isSunk()){
                System.out.println("Hundiste un barco");
                playerScore = playerScore + 1;
                gameController.drawSunkShips(targetShip,opponentGrid);
            }
            gameController.saveGameState();
            gameController.checkWinCondition();
        }
        else{
            onTurnEnd.run();
        }

    }
}
