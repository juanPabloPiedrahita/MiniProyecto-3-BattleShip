package com.example.miniproyecto3.model.Players;

import com.example.miniproyecto3.model.exception.DoubleShootException;
import com.example.miniproyecto3.controller.GameController;
import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;
import javafx.application.Platform;
import javafx.scene.layout.*;

import java.io.Serializable;
import java.util.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class AI extends IPlayerAdapter implements Serializable{

    private List<Ship.Coordinate> pendingTargets = new ArrayList<>();
    private final Random rand = new Random();
    private int score;
    private String name;
    private String dificulty;

    public AI(int score,String name,String dificulty) {
        this.score = score;
        this.name = name;
        this.dificulty = dificulty;
    }

    @Override
    public void makeMove(int row1, int col1, Board ownBoard, Board opponentBoardModel,
                         GridPane opponentGrid, Runnable onTurnEnd,
                         List<Ship> playerShips, GameController gameController) {

        int row, col;

        if (!pendingTargets.isEmpty()) {
            Ship.Coordinate coordinate = pendingTargets.remove(0);
            row = coordinate.getRow();
            col = coordinate.getCol();
        } else {
            do {
                row = rand.nextInt(10);
                col = rand.nextInt(10);
            } while (ownBoard.alreadyShotAt(row, col, false));
        }

        ownBoard.registerShot(row, col, false);

        StackPane cell = gameController.getStackPaneAt(opponentGrid, row, col);

        boolean hit = ownBoard.hasShipAt(row, col, true);
        Canvas c = new Canvas(30, 30);
        GraphicsContext gc = c.getGraphicsContext2D();
        c.setMouseTransparent(true);
        c.setUserData(hit ? "impacto" : "fallo");
        gameController.drawShot(gc, hit, false);
        cell.getChildren().add(c);

        if (hit) {
            Ship s = gameController.getShipAt(playerShips, row, col);
            s.registerHit(row, col);
            if (s.isSunk()) {
                gameController.drawSunkShips(s, opponentGrid);
                pendingTargets.clear();
                ownBoard.removeShip(s, true);  // Acá sólo se usa ownBoard también, XD.
                gameController.debugBoards();
            } else {
                addAdjacentTargets(row, col, ownBoard);
            }

            gameController.saveGameState();
            gameController.checkWinCondition();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> {
                        makeMove(row1, col1, ownBoard, opponentBoardModel,
                                opponentGrid, onTurnEnd, playerShips, gameController);
                    });
                }
            }, 1000);
        } else {
            onTurnEnd.run();
        }
    }

    private void addAdjacentTargets(int row, int col, Board ownBoard) {
        List<Ship.Coordinate> directions = List.of(
          new Ship.Coordinate(-1, 0),
          new Ship.Coordinate(1, 0),
          new Ship.Coordinate(0, -1),
          new Ship.Coordinate(0, 1)
        );

        for(Ship.Coordinate dir : directions){
            int r = row + dir.getRow();
            int c = col + dir.getCol();

            if(r >= 0 && r < 10 && c >= 0 && c < 10 && !ownBoard.alreadyShotAt(r, c, false)){
                pendingTargets.add(new Ship.Coordinate(r, c));
            }
        }
    }


    public void makeRandomMove(Board ownBoard, Board playerBoard, GridPane opponentGridPane, List<Ship> playerShips, Runnable onTurnEnd , GameController gameController){
        int row, col;
        do {
            row = rand.nextInt(10);
            col = rand.nextInt(10);
        } while (ownBoard.alreadyShotAt(row, col, false));

        ownBoard.registerShot(row, col, false);
        boolean hit = ownBoard.hasShipAt(row, col, true);
        StackPane cell = gameController.getStackPaneAt(opponentGridPane, row, col);

        Canvas canvas = new Canvas(30,30);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setMouseTransparent(true);
        canvas.setUserData(hit ? "impacto" : "fallo");
        gameController.drawShot(gc, hit, false);
        cell.getChildren().add(canvas);

        Ship targetShip = gameController.getShipAt(playerShips, row, col);
        if(hit) {
            targetShip.registerHit(row, col);
            if (targetShip.isSunk()) {
                gameController.drawSunkShips(targetShip, opponentGridPane);
                ownBoard.removeShip(targetShip, true);   // Acá sólo se usa ownBoard también, XD.
                gameController.debugBoards();
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> {
                        Platform.runLater(() -> makeRandomMove(ownBoard, playerBoard, opponentGridPane, playerShips, onTurnEnd, gameController));

                    });
                }
            }, 1000);
        }
        else{
            onTurnEnd.run();
        }
    }

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
