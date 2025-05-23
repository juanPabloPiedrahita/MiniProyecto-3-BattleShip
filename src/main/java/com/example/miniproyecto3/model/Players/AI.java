package com.example.miniproyecto3.model.Players;

import com.example.miniproyecto3.controller.GameController;
import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;
import javafx.application.Platform;
import javafx.scene.layout.*;

import java.util.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class AI implements IPlayer {

    private final List<int[]> pendingTargets = new ArrayList<>();
    private final Random rand = new Random();
    private int score;
    private String name;

    public AI(int score,String name) {
        this.score = score;
        this.name = name;
    }

    @Override
    public void makeMove(Board ownBoard, Board opponentBoardModel, GridPane opponentGrid, Runnable onTurnEnd, List<Ship> playerShips, GameController gameController) {
        // 1) Selección de coordenada
        int row, col;
        if (!pendingTargets.isEmpty()) {
            int[] t = pendingTargets.remove(0);
            row = t[0];
            col = t[1];
        } else {
            do {
                row = rand.nextInt(10);
                col = rand.nextInt(10);
            } while (ownBoard.alreadyShotAt(row, col, false));
        }

        // 2) Registrar tiro
        ownBoard.registerShot(row, col, false);

        // 3) Obtener la celda UI
        StackPane cell = gameController.getStackPaneAt(opponentGrid, row, col);

        // 4) Dibujar impacto o fallo
        boolean hit = ownBoard.hasShipAt(row, col, true);
        Canvas c = new Canvas(30, 30);
        GraphicsContext gc = c.getGraphicsContext2D();
        c.setMouseTransparent(true);
        c.setUserData(hit ? "impacto" : "fallo");
        gameController.drawShot(gc, hit, false);
        cell.getChildren().add(c);

        // 5) Si impacto, coordinar hunt-mode
        if (hit) {
            Ship s = gameController.getShipAt(playerShips, row, col);
            s.registerHit(row, col);
            if (s.isSunk()) {
                // marcar hundido
                gameController.drawSunkShips(s, opponentGrid);
                pendingTargets.clear();
                //Platform.runLater(() -> makeMove(ownBoard, opponentBoardModel, opponentGrid, onTurnEnd, playerShips, gameController));
            } else {
                addAdjacentTargets(row, col, ownBoard);
                // vuelve a disparar:
                //Platform.runLater(() -> makeMove(ownBoard, opponentBoardModel, opponentGrid, onTurnEnd, playerShips, gameController));
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> {
                        Platform.runLater(() -> makeMove(ownBoard, opponentBoardModel, opponentGrid, onTurnEnd, playerShips, gameController));

                    });
                }
            }, 1000);
        } else {
            // fin de turno
            onTurnEnd.run();
        }
    }

    private void addAdjacentTargets(int row, int col, Board ownBoard) {
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : dirs) {
            int r = row + d[0], c = col + d[1];
            if (r >= 0 && r < 10 && c >= 0 && c < 10 && !ownBoard.alreadyShotAt(r, c, false)) {
                pendingTargets.add(new int[]{r, c});
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
}
