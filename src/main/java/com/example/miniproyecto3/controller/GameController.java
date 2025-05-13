package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.Ship;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;

import java.util.*;

public class GameController {

    @FXML private GridPane playerBoard;
    @FXML private GridPane enemyBoard;
    @FXML private ComboBox<Integer> shipSizeSelector;
    @FXML private ToggleButton orientationToggle;
    @FXML private Button monitorButton;

    private Board playerBoardModel = new Board();
    private Board enemyBoardModel = new Board();
    private List<Ship> playerShips = new ArrayList<>();
    private List<Ship> enemyShips = new ArrayList<>();
    private StackPane[][] enemyCells = new StackPane[10][10];

    private boolean finishedPlacing = false;
    private boolean monitorMode = false;
    private boolean playerTurn = true;
    private boolean gameEnded = false;

    @FXML
    public void initialize() {
        createBoard(playerBoard, true);
        createBoard(enemyBoard, false);
        shipSizeSelector.getSelectionModel().selectFirst();
        orientationToggle.setOnAction(e -> toggleOrientation());
        monitorButton.setDisable(true);
    }

    private void createBoard(GridPane board, boolean isPlayer) {
        board.getChildren().clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                StackPane cell = new StackPane();
                Button btn = new Button();
                btn.setMinSize(30, 30);
                btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                btn.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
                cell.getChildren().add(btn);

                if(!isPlayer) {
                    enemyCells[row][col] = cell;
                }

                int finalRow = row;
                int finalCol = col;
                btn.setOnMouseClicked(e -> {
                    if (!finishedPlacing && isPlayer) {
                        placePlayerShip(finalRow, finalCol);
                    } else if (finishedPlacing && !isPlayer && e.getButton() == MouseButton.PRIMARY) {
                        handlePlayerShot(finalRow, finalCol);
                    }
                });

                board.add(cell, col, row);
            }
        }
    }

    private void toggleOrientation() {
        if (orientationToggle.isSelected()) {
            orientationToggle.setText("Vertical");
        } else {
            orientationToggle.setText("Horizontal");
        }
    }

    private void placePlayerShip(int row, int col) {
        int size = shipSizeSelector.getValue();
        boolean horizontal = !orientationToggle.isSelected();
        Ship ship = playerBoardModel.placeShip(row, col, size, horizontal, true);

        if (ship != null) {
            playerShips.add(ship);
            placeShipVisual(playerBoard, ship, Color.DARKGRAY);
            shipSizeSelector.getItems().remove((Integer) size);

            if (shipSizeSelector.getItems().isEmpty()) {
                shipSizeSelector.setDisable(true);
                orientationToggle.setDisable(true);
                monitorButton.setDisable(false);
            }
        }
    }

    private void placeShipVisual(GridPane board, Ship ship, Color color) {
        //for (int[] coord : ship.getCoordinates()) {
            //int row = coord[0];
            //int col = coord[1];
            //StackPane cell = getStackPaneAt(board, row, col);
            //if(cell != null) {
                /*Canvas canvas = new Canvas(30, 30);
                GraphicsContext gc = canvas.getGraphicsContext2D();

                if(ship.isHorizontal()) {
                    gc.setFill(color);
                    gc.fillRect(0, 0, 30 * ship.getSize(), 30);
                } else {
                    gc.setFill(color);
                    gc.fillRect(0, 0, 30, 30 * ship.getSize());
                }

                cell.getChildren().add(canvas);*/
                //Rectangle rect = new Rectangle(30, 30);
                //rect.setFill(color);
                //cell.getChildren().add(rect);
            //}


        //}
        List<int[]> coords = ship.getCoordinates();

        for(int i = 0; i < coords.size(); i++) {
            int[] coord = coords.get(i);
            int row = coord[0];
            int col = coord[1];

            boolean isFirst = (i == 0);
            boolean isLast = (i == coords.size() - 1);

            StackPane cell = getStackPaneAt(board, row, col);
            if(cell != null) {
                Canvas canvas = new Canvas(30, 30);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                drawBoatShape(gc, ship.isHorizontal(), isFirst, isLast, color);
                canvas.setMouseTransparent(true);
                cell.getChildren().add(canvas);
            }
        }
    }

    private void placeShipVisualHidden(GridPane board, Ship ship) {
        for (int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            StackPane cell = getStackPaneAt(board, row, col);

            if(cell != null) {
                Rectangle rect = new Rectangle(30, 30);
                rect.setFill(Color.LIGHTGREEN);
                rect.setVisible(monitorMode); // Se muestra solo si está en modo monitor
                rect.setMouseTransparent(true);
            //rect.toBack();
            //rect.setVisible(false);
                cell.getChildren().add(rect);
                /*Canvas canvas = new Canvas(30, 30);
                GraphicsContext gc = canvas.getGraphicsContext2D();

                if(ship.isHorizontal()) {
                    gc.setFill(Color.LIGHTGREEN);
                    gc.fillRect(0, 0, 30 * ship.getSize(), 30);
                } else {
                    gc.setFill(Color.LIGHTGREEN);
                    gc.fillRect(0, 0, 30, 30 * ship.getSize());
                }

                canvas.setVisible(monitorMode);
                canvas.setMouseTransparent(true);
                cell.getChildren().add(canvas);

                Label marker = new Label("");
                marker.setFont(javafx.scene.text.Font.font("Arial", FontWeight.BOLD, 15));
                //assert cell != null;
                cell.getChildren().add(marker);*/


            }

            /*Rectangle rect = new Rectangle(30, 30);
            rect.setFill(Color.LIGHTGREEN);
            rect.setVisible(monitorMode); // Se muestra solo si está en modo monitor
            rect.setMouseTransparent(true);
            //rect.toBack();
            //rect.setVisible(false);
            cell.getChildren().add(rect);*/


            //enemyBoard.add(cell, col, row);
        }
    }

    private StackPane getStackPaneAt(GridPane board, int row, int col) {
        for (javafx.scene.Node node : board.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (StackPane) node;
            }
        }
        return null;
    }

    private void handlePlayerShot(int row, int col) {
        if(!playerTurn || gameEnded) return;

        StackPane cell = getStackPaneAt(enemyBoard, row, col);
        //assert cell != null;
        if(cell == null) return;

        for(javafx.scene.Node child : cell.getChildren()) {
            if(child instanceof Label label && (label.getText().equals("X") || label.getText().equals("O"))) {
                return;
            }
        }
        //Button btn = (Button) cell.getChildren().get(0);

        //if (!btn.getText().isEmpty()) return;

        Ship hitShip = enemyBoardModel.shoot(row, col, true);
        if (hitShip != null) {
            System.out.println("Shot at " + row + ", " + col);
            //btn.setDisable(true);
            //cell.getChildren().remove(btn);
            Label hitLabel = new Label("X");
            hitLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red; -fx-font-weight: bold;");
            cell.getChildren().add(hitLabel);

            if(hitShip.isSunk()) {
                highlightSunkShip(hitShip);
            }
            //btn.setDisable(true);
            //btn.setText("X");
            //btn.setStyle("-fx-background-color: red;");
        } else {
            Label missLabel = new Label("O");
            missLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: blue; -fx-font-weight: bold;");
            cell.getChildren().add(missLabel);
            //btn.setText("O");
            //btn.setStyle("-fx-background-color: white;");
        }

        playerTurn = false;
        checkWinCondition();

        if(gameEnded) return;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    handleComputerShot();
                    playerTurn = true;
                    checkWinCondition();
                });

            }
        }, 1000);
    }

    private void highlightSunkShip(Ship ship) {
        for(int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            StackPane cell = getStackPaneAt(enemyBoard, row, col);

            if(cell != null) {
                /*Canvas canvas = new Canvas(30, 30);
                GraphicsContext gc = canvas.getGraphicsContext2D();

                gc.setFill(Color.LIGHTPINK);
                gc.setGlobalAlpha(0.6);
                gc.fillRect(0, 0, 30, 30);
                gc.setGlobalAlpha(1);

                canvas.setMouseTransparent(true);
                cell.getChildren().add(canvas);*/
                Rectangle burnMark = new Rectangle(30, 30);
                burnMark.setFill(Color.LIGHTPINK);
                burnMark.setOpacity(0.6);
                burnMark.setMouseTransparent(true);
                cell.getChildren().add(burnMark);
                //cell.setStyle("-fx-background-color: red;");
                //cell.setMouseTransparent(true);
                //cell.toBack();
                //cell.setVisible(false);
                //cell.setDisable(true);
                //cell.setOpacity(0.5);
                //cell.setMouseTransparent(true);
            }
        }
    }

    private void highlightPlayerSunkShip(Ship ship) {
        for(int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            StackPane cell = getStackPaneAt(playerBoard, row, col);

            if(cell != null) {
                /*Canvas canvas = new Canvas(30, 30);
                GraphicsContext gc = canvas.getGraphicsContext2D();

                gc.setFill(Color.LIGHTPINK);
                gc.setGlobalAlpha(0.6);
                gc.fillRect(0, 0, 30, 30);
                gc.setGlobalAlpha(1);

                canvas.setMouseTransparent(true);
                cell.getChildren().add(canvas);*/
                Rectangle burnMark = new Rectangle(30, 30);
                burnMark.setFill(Color.LIGHTPINK);
                burnMark.setOpacity(0.6);
                burnMark.setMouseTransparent(true);
                cell.getChildren().add(burnMark);
                //cell.setStyle("-fx-background-color: red;");
                //cell.setMouseTransparent(true);
                //cell.toBack();
                //cell.setVisible(false);
                //cell.setDisable(true);
                //cell.setOpacity(0.5);
                //cell.setMouseTransparent(true);
            }
        }
    }

    private void handleComputerShot() {
        Random rand = new Random();
        int row, col;

        do{
            row = rand.nextInt(10);
            col = rand.nextInt(10);
        } while (playerBoardModel.alreadyShotAt(row, col, false));



        playerBoardModel.registerShot(row, col, false);

        StackPane cell = getStackPaneAt(playerBoard, row, col);
        if(cell == null) return;

        if(playerBoardModel.hasShipAt(row, col, true)) {
            Ship hitShipAtPosition = getShipAt(playerShips, row, col);
            if(hitShipAtPosition != null) {
                System.out.println("Shot at " + row + ", " + col);
                hitShipAtPosition.registerHit(row, col);
                Label hitMachineLabel = new Label("X");
                hitMachineLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red; -fx-font-weight: bold;");
                cell.getChildren().add(hitMachineLabel);
                if(hitShipAtPosition.isSunk()) {
                    highlightPlayerSunkShip(hitShipAtPosition);
                }
            }
        } else {
            Label missMachineLabel = new Label("O");
            missMachineLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: blue; -fx-font-weight: bold;");
            cell.getChildren().add(missMachineLabel);
        }
    }

    private Ship getShipAt(List<Ship> ships, int row, int col) {
        for(Ship ship : ships) {
            if(ship.occupies(row, col)) {
                return ship;
            }
        }
        return null;
    }

    private void checkWinCondition() {
        if(gameEnded) return;

        boolean allEnemySunk = enemyShips.stream().allMatch(Ship::isSunk);
        boolean allPlayerSunk = playerShips.stream().allMatch(Ship::isSunk);

        if(allEnemySunk) {
            gameEnded = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "¡Ganaste! Has hundido todos los barcos enemigos.");
            alert.showAndWait();
            playerTurn = false;
        } else if(allPlayerSunk) {
            gameEnded = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "¡Has perdido! La máquina ha hundido todos tus barcos.");
            alert.showAndWait();
            playerTurn = false;
        }
    }

    @FXML
    private void handleFinishPlacement() {
        if (shipSizeSelector.getItems().isEmpty()) {
            finishedPlacing = true;
            placeEnemyShips();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Coloca todos los barcos antes de continuar.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void placeEnemyShips() {
        Random rand = new Random();
        List<Integer> shipSizes = Arrays.asList(2, 3, 4, 5);

        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int row = rand.nextInt(10);
                int col = rand.nextInt(10);
                boolean horizontal = rand.nextBoolean();
                Ship ship = enemyBoardModel.placeShip(row, col, size, horizontal, false);
                if (ship != null) {
                    enemyShips.add(ship);
                    placeShipVisualHidden(enemyBoard, ship);
                    placed = true;
                }
            }

        }
        debugEnemyBoard();
    }

    @FXML
    private void toggleEnemyBoard() {
        monitorMode = !monitorMode;
        for (javafx.scene.Node node : enemyBoard.getChildren()) {
            if (node instanceof StackPane) {
                StackPane cell = (StackPane) node;
                for (javafx.scene.Node child : cell.getChildren()) {
                    if (child instanceof Rectangle rect && rect.getFill().equals(Color.LIGHTGREEN)) {
                        rect.setVisible(monitorMode);
                    }
                }
            }
        }
    }

    private void drawBoatShape(GraphicsContext gc, boolean horizontal, boolean isFirst, boolean isLast, Color color) {
        gc.clearRect(0, 0, 30, 30);

        gc.setFill(color);
        /*double boatWidth = 20;
        double boatHeight = 20;

        if(horizontal) {
            gc.fillRoundRect(5, 5, boatWidth * 2, boatHeight, 10, 10);
        } else {
            gc.fillRoundRect(5, 5, boatWidth, boatHeight * 2, 10, 10);
        }
        //gc.fillRoundRect(5, 5, 20, 20, 10, 10);

        gc.setFill(color.darker());
        if(horizontal) {
            gc.fillRoundRect(10, 10, boatWidth * 2, boatHeight - 5, 10, 10);
        } else {
            gc.fillRoundRect(10, 10, boatWidth - 5, boatHeight * 2, 10, 10);
        }*/

        if(isFirst) {
            gc.setFill(color.darker());
            if(horizontal) {
                gc.fillPolygon(new double[]{25, 5, 5}, new double[]{0, 30, 15}, 3);
            } else {
                gc.fillPolygon(new double[]{0, 30, 15}, new double[]{5, 5, 25}, 3);
            }
        }

        else if(isLast) {
            gc.setFill(color.darker());
            if(horizontal) {
                //gc.fillPolygon(new double[]{30, 25, 25}, new double[]{15, 5, 25}, 3);
                gc.fillRoundRect(5, 5, 20, 20, 10, 10);
                gc.setFill(Color.GRAY);
                gc.fillOval(8, 8, 5, 5);
            } else {
                //gc.fillPolygon(new double[]{15, 5, 25}, new double[]{30, 25, 25}, 3);
                gc.fillRoundRect(5, 5, 20, 20, 10, 10);
                gc.setFill(Color.GRAY);
                gc.fillOval(8, 8, 5, 5);
            }
        } else {
            gc.setFill(color);
            gc.fillRect(5, 5, 20, 20);

            gc.setStroke(color.darker());
            gc.setLineWidth(2);
            if(horizontal) {
                gc.strokeLine(10, 5, 10, 25);
                gc.strokeLine(20, 5, 20, 25);
            } else {
                gc.strokeLine(5, 10, 25, 10);
                gc.strokeLine(5, 20, 25, 20);
            }

            gc.setFill(Color.BLACK);
            gc.fillOval(13, 13, 4, 4);
        }

        /*gc.setFill(color.brighter());
        if(horizontal) {
            gc.fillRoundRect(15, 5, 10, 10, 5, 5);
        } else {
            gc.fillRoundRect(5, 15, 10, 10, 5, 5);
        }

        gc.setFill(Color.BLACK);
        gc.fillOval(13, 13, 4, 4);*/
    }

    private void debugEnemyBoard() {
        System.out.println("=== DEBUG: enemyBoardModel ===");
        boolean[][] enemyGrid = enemyBoardModel.getEnemyBoard();

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                boolean isShip = enemyGrid[row][col];  // No afecta lógica si solo se consulta
                System.out.print(isShip ? "[X]" : "[ ]");
            }
            System.out.println();  // Salto de línea por fila
        }
    }

}

