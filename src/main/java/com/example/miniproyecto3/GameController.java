package com.example.miniproyecto3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameController {

    @FXML private GridPane playerBoard;
    @FXML private GridPane enemyBoard;
    @FXML private ComboBox<Integer> shipSizeSelector;
    @FXML private ToggleButton orientationToggle;
    @FXML private Button monitorButton;

    private boolean placementFinished = false;
    private List<Ship> playerShips = new ArrayList<>();
    private List<Ship> enemyShips = new ArrayList<>();
    private Set<String> playerShots = new HashSet<>();
    private Set<String> enemyShots = new HashSet<>();

    private static final int BOARD_SIZE = 10; // Tamaño del tablero

    @FXML
    public void initialize() {
        createBoard(playerBoard, false);
        createBoard(enemyBoard, true);
        enemyBoard.setVisible(false);

        orientationToggle.setOnAction(e -> {
            if (orientationToggle.isSelected()) {
                orientationToggle.setText("Vertical");
            } else {
                orientationToggle.setText("Horizontal");
            }
        });
    }

    // Crear el tablero con botones
    private void createBoard(GridPane board, boolean isEnemy) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button btn = new Button();
                btn.setMinSize(30, 30);
                btn.setStyle("-fx-background-color: lightblue;");
                int finalRow = row;
                int finalCol = col;
                btn.setOnAction(e -> handlePlayerClick(finalRow, finalCol, btn));

                if (isEnemy) {
                    int finalRow1 = row;
                    int finalCol1 = col;
                    btn.setOnAction(e -> handleEnemyClick(finalRow1, finalCol1, btn));
                }

                board.add(btn, col, row);
            }
        }
    }

    // Manejar clic en el tablero del jugador
    private void handlePlayerClick(int row, int col, Button cell) {
        if (placementFinished) return;

        Integer size = shipSizeSelector.getValue();
        if (size == null) return;

        boolean horizontal = !orientationToggle.isSelected();

        if (canPlaceShip(row, col, size, horizontal, playerBoard)) {
            Ship ship = new Ship(size, row, col, horizontal);
            playerShips.add(ship);
            placeShipVisual(ship, playerBoard);
        }
    }

    private boolean canPlaceShip(int row, int col, int size, boolean horizontal, GridPane board) {
        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            if (r >= BOARD_SIZE || c >= BOARD_SIZE) return false;

            Button btn = getButtonAt(board, r, c);
            if (btn.getStyle().contains("gray")) return false;
        }
        return true;
    }

    private void placeShipVisual(Ship ship, GridPane board) {
        for (int i = 0; i < ship.size; i++) {
            int r = ship.startRow + (ship.horizontal ? 0 : i);
            int c = ship.startCol + (ship.horizontal ? i : 0);
            Button btn = getButtonAt(board, r, c);
            btn.setStyle("-fx-background-color: gray;");
        }
    }

    private Button getButtonAt(GridPane grid, int row, int col) {
        for (javafx.scene.Node node : grid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (Button) node;
            }
        }
        return null;
    }

    @FXML
    private void handleFinishPlacement() {
        placementFinished = true;
        System.out.println("Colocación terminada. Comienza la batalla.");
        generateEnemyShips();
    }

    private void generateEnemyShips() {
        Random random = new Random();
        int[] shipSizes = {5, 4, 3, 3, 2}; // Tamaño de los barcos enemigos
        enemyShips.clear();

        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                boolean horizontal = random.nextBoolean();
                int row = random.nextInt(BOARD_SIZE);
                int col = random.nextInt(BOARD_SIZE);

                if (canPlaceShip(row, col, size, horizontal, enemyBoard)) {
                    Ship ship = new Ship(size, row, col, horizontal);
                    enemyShips.add(ship);
                    placeShipVisualHidden(ship, enemyBoard);
                    placed = true;
                }
            }
        }
    }

    private void placeShipVisualHidden(Ship ship, GridPane board) {
        for (int i = 0; i < ship.size; i++) {
            int r = ship.startRow + (ship.horizontal ? 0 : i);
            int c = ship.startCol + (ship.horizontal ? i : 0);
            Button btn = getButtonAt(board, r, c);
            // Si quieres verlas para monitoreo:
            // btn.setStyle("-fx-background-color: darkgray;");
        }
    }

    // Disparos del jugador
    private void handleEnemyClick(int row, int col, Button cell) {
        if (!placementFinished || playerShots.contains(row + "-" + col)) return;

        playerShots.add(row + "-" + col);

        if (isHit(row, col, enemyShips)) {
            cell.setText("O");
            cell.setStyle("-fx-background-color: red;");
        } else {
            cell.setText("X");
            cell.setStyle("-fx-background-color: lightblue;");
        }

        cell.setDisable(true);

        // Turno de la máquina
        enemyShoot();
    }

    // Disparo de la máquina
    private void enemyShoot() {
        Random rand = new Random();
        int row, col;
        String key;
        do {
            row = rand.nextInt(BOARD_SIZE);
            col = rand.nextInt(BOARD_SIZE);
            key = row + "-" + col;
        } while (enemyShots.contains(key));

        enemyShots.add(key);

        Button cell = getButtonAt(playerBoard, row, col);

        if (isHit(row, col, playerShips)) {
            cell.setText("O");
            cell.setStyle("-fx-background-color: darkred;");
        } else {
            cell.setText("X");
            cell.setStyle("-fx-background-color: lightgray;");
        }

        cell.setDisable(true);
    }

    private boolean isHit(int row, int col, List<Ship> ships) {
        for (Ship ship : ships) {
            for (int i = 0; i < ship.size; i++) {
                int r = ship.startRow + (ship.horizontal ? 0 : i);
                int c = ship.startCol + (ship.horizontal ? i : 0);
                if (r == row && c == col) {
                    return true;
                }
            }
        }
        return false;
    }

    @FXML
    private void toggleEnemyBoard() {
        boolean isVisible = enemyBoard.isVisible();
        enemyBoard.setVisible(!isVisible);
        monitorButton.setText(isVisible ? "Mostrar Tablero Enemigo" : "Ocultar Tablero Enemigo");
    }
}

