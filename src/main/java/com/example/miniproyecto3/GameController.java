package com.example.miniproyecto3;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

    private boolean finishedPlacing = false;
    private boolean monitorMode = false;

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
        Ship ship = new Ship(size, row, col, horizontal);

        if (playerBoardModel.placeShip(ship.startRow, ship.startCol, ship.size, ship.horizontal, true)) {
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
        for (int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            StackPane cell = getStackPaneAt(board, row, col);

            Rectangle rect = new Rectangle(30, 30);
            rect.setFill(color);
            cell.getChildren().add(rect);
        }
    }

    private void placeShipVisualHidden(GridPane board, Ship ship) {
        for (int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            StackPane cell = getStackPaneAt(board, row, col);

            Rectangle rect = new Rectangle(30, 30);
            rect.setFill(Color.LIGHTGREEN);
            rect.setVisible(monitorMode); // Se muestra solo si está en modo monitor
            cell.getChildren().add(rect);
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
        StackPane cell = getStackPaneAt(enemyBoard, row, col);
        Button btn = (Button) cell.getChildren().get(0);

        if (!btn.getText().isEmpty()) return;

        if (enemyBoardModel.shoot(row, col)) {
            btn.setText("X");
            btn.setStyle("-fx-background-color: red;");
        } else {
            btn.setText("O");
            btn.setStyle("-fx-background-color: white;");
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
                Ship ship = new Ship(size, row, col, horizontal);
                if (enemyBoardModel.placeShip(ship.startRow, ship.startCol, ship.size, ship.horizontal, true)) {
                    enemyShips.add(ship);
                    placeShipVisualHidden(enemyBoard, ship);
                    placed = true;
                }
            }
        }
    }

    @FXML
    private void toggleEnemyBoard() {
        monitorMode = !monitorMode;
        for (javafx.scene.Node node : enemyBoard.getChildren()) {
            if (node instanceof StackPane) {
                StackPane cell = (StackPane) node;
                for (javafx.scene.Node child : cell.getChildren()) {
                    if (child instanceof Rectangle) {
                        child.setVisible(monitorMode);
                    }
                }
            }
        }
    }
}

