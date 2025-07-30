package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.GameState;
import com.example.miniproyecto3.model.Ship;
import com.example.miniproyecto3.exceptions.DoubleShootException;
import com.example.miniproyecto3.exceptions.VisualException;
import com.example.miniproyecto3.view.GameStage;
import com.example.miniproyecto3.view.WelcomeStage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.Node;
import com.example.miniproyecto3.model.serializable.SerializableFileHandler;
import com.example.miniproyecto3.model.planeTextFiles.PlaneTextFileHandler;
import com.example.miniproyecto3.model.MusicPlayer;
import com.example.miniproyecto3.model.Players.Player;
import java.io.File;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import com.example.miniproyecto3.model.Players.AI;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller class for managing the core gameplay logic and visual components
 * of the Battleship game. This class is responsible for:
   - Initializing boards and ships for both player and AI.
   - Handling GUI events via FXML.
   - Managing turn-taking logic.
   - Handling AI difficulty and ship placement.
   - Tracking game state for serialization and continuation.
   - Coordinating music playback.

 * @author David Taborda Montenegro and Juan Pablo Piedrahita Triana.
 * @version 3.2
 * @since version 1.0

 * @see GameStage
 * @see Player
 * @see AI
 * @see Board
 * @see Ship
 * @see SerializableFileHandler
 * @see PlaneTextFileHandler
 */
public class GameController {

    /**
     * The FXML objects to make references, allowing to control the visual flow of the game.
     */
    @FXML
    private GridPane playerBoard;
    @FXML
    private GridPane enemyBoard;
    @FXML
    private VBox shipSelectorContainer;
    @FXML
    private ToggleButton orientationToggle;
    @FXML
    private Button monitorButton;
    @FXML
    private Button readyButton;
    @FXML
    private VBox placementControls;
    @FXML
    private VBox enemyBoardContainer;
    @FXML
    private Label label1;
    @FXML
    private Label difficultyLabel;
    @FXML
    private ComboBox<String> difficultySelector;
    @FXML
    private Label configLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Label exceptLabel;
    @FXML
    private Label labelScore;

    /**
     * Logic elements to control the logic behavior of the game.
     */
    private Board playerBoardModel = new Board();
    private Board enemyBoardModel = new Board();
    private List<Ship> playerShips = new ArrayList<>();
    private List<Ship> enemyShips = new ArrayList<>();

    /**
     * Boolean attributes for the control of the current state of the game.
     */
    private boolean finishedPlacing = false;
    private boolean monitorMode = false;
    private boolean playerTurn = true;
    private boolean gameEnded = false;

    /** Attribute for manage the plane text file savings. */
    private PlaneTextFileHandler planeTextFileHandler;

    /** Object that represents the human player. */
    private Player player;

    /** Boolean attribute for check if the player wants to continue a saved game. */
    private boolean continueGame;

    /** Boolean attribute for save the difficulty that the machine will implement. */
    private String selectedDifficulty;

    /**
     * For the visual interactions with the logic.
     */
    private Image explosion;
    private Image miss;
    private Image smoke;

    /** Object for the music background. */
    MusicPlayer musicPlayer;

    /**
     * For the selection screen.
     */
    private Canvas selectedShipCanvas = null;
    private int selectedShipSize = 0;

    /**
     * Elements that allow the float configuration.
     */
    private final Map<Integer, Integer> fleetComposition = Map.of(
            1, 4,
            2, 3,
            3, 2,
            4, 1
    );
    private Map<Integer, Image> shipImages;
    private final Map<Integer, Integer> placedShipsCount = new HashMap<>();
    private final Map<Integer, HBox> shipRowMap = new HashMap<>();

    /** Object that represents the machine. */
    private AI enemy = new AI(0,"Enemy", "Fácil");

    /**
     * Initializes the game controller. It sets up the UI components, initializes
     * resources such as images and music, and handles logic for starting a new game
     * or continuing a saved one.
     * It distinguishes between a new game and a resumed game using the 'continueGame'
     * flag, and performs corresponding setup actions such as enabling or disabling
     * controls, loading images, creating boards, and setting up the ship selector.

     * If an image file cannot be loaded, it manages an IOException.
     * If a problem occurs when loading a saved game, it manages a VisualException.
     * @see com.example.miniproyecto3.exceptions.VisualException
     */
    @FXML
    public void initialize() {
        try {
                labelScore.setVisible(false);
                enemyBoard.getStyleClass().add("grid-pane");
                playerBoard.getStyleClass().add("grid-pane");
                label1.getStyleClass().add("enemy-turn-label");
                musicPlayer = new MusicPlayer("/com/example/miniproyecto3/Media/SelectionTheme.mp3");
                musicPlayer.play();
                planeTextFileHandler = new PlaneTextFileHandler();
                player = WelcomeStage.getInstance().getWelController().getPlayer();
                label1.setText("Tu tablero, " + player.getPlayerName() + ":");
                System.out.println("Player: " + player.getPlayerName() + ", " + player.getPlayerScore());
                continueGame = WelcomeStage.getInstance().getWelController().getContinue();
                WelcomeStage.deleteInstance();
                smoke = loadImageOrThrow("/com/example/miniproyecto3/Image/blackSmoke23.png");
                miss = loadImageOrThrow("/com/example/miniproyecto3/Image/waterExplosion.png");
                explosion = loadImageOrThrow("/com/example/miniproyecto3/Image/explosion08.png");
                shipImages = new HashMap<>();
                shipImages.put(1, loadImageOrThrow("/com/example/miniproyecto3/Image/prueba8.png"));
                shipImages.put(2, loadImageOrThrow("/com/example/miniproyecto3/Image/prueba14.png"));
                shipImages.put(3, loadImageOrThrow("/com/example/miniproyecto3/Image/prueba4.png"));
                shipImages.put(4, loadImageOrThrow("/com/example/miniproyecto3/Image/prueba10.png"));

                if (!continueGame) {
                    System.out.println("Nuevo juego...");
                    System.out.println("Creando playerboard");
                    createBoard(playerBoard, true);
                    System.out.println("Creando enemyboard");
                    createBoard(enemyBoard, false);
                    orientationToggle.setOnAction(_ -> toggleOrientation());
                    monitorButton.setDisable(true);
                    placementControls.setVisible(true);
                    placementControls.setManaged(true);
                    enemyBoardContainer.setVisible(false);
                    enemyBoardContainer.setManaged(false);
                    monitorButton.setVisible(false);
                    monitorButton.setManaged(false);
                    initializeShipSelectorCanvases();
                } else {
                    System.out.println("Entrando a cargar el juego mas reciente");
                    finishedPlacing = true;
                    loadGameState();
                    musicPlayer = new MusicPlayer("/com/example/miniproyecto3/Media/RedAlert3Theme.mp3");
                    musicPlayer.play();
                    readyButton.setDisable(true);
                    orientationToggle.setDisable(true);
                    placementControls.setVisible(false);
                    placementControls.setManaged(false);
                    enemyBoardContainer.setVisible(true);
                    enemyBoardContainer.setManaged(true);
                    monitorButton.setVisible(true);
                    monitorButton.setManaged(true);
                    labelScore.setVisible(true);
                    labelScore.setText("Tu puntaje:" + player.getPlayerScore());
                }
        } catch (IOException e) {
            System.out.println("Error cargando el juego." + e.getMessage());
        } catch (VisualException ex) {
            System.out.println("Ocurrió un error al cargar el juego." + ex.getMessage());
        }
    }

    /**
     * Loads an image from the given resource path. If the resource is not found,
     * an IOException is thrown.
     *
     * @param path the path to the image resource.
     * @return the loaded {@link Image}.
     * @throws IOException if the image resource could not be found or accessed.
     */
    private Image loadImageOrThrow(String path) throws IOException {
        URL resourceUrl = getClass().getResource(path);
        if(resourceUrl == null) {
            throw new IOException("No se pudo encontrar el recurso: " + path);
        }
        return new Image(resourceUrl.toExternalForm());
    }

    /**
     * Creates a 10x10 battleship board within the specified {@link GridPane}.
     * It sets up column and row constraints, adds labels for coordinates (A - J for the columns; 1 – 10 for the rows),
     * and fills the grid with buttons. Each button is placed inside a {@link StackPane}
     * for better control of visual stacking and click handling.

     * If the board is for the player, buttons are configured to place ships during the
     * setup phase. If the board is for the machine, buttons are configured to handle
     * shooting logic after setup is completed.
     *
     * @param board the GridPane that will represent the game board.
     * @param isPlayer True if the board belongs to the player, false if it is the enemy board.
     * @throws IOException if an error occurs during board setup.
     * @see UIVisualHelper
     */
    private void createBoard(GridPane board, boolean isPlayer) throws IOException{
        System.out.println("Creando el tablero del jugador.");
        for (int i = 0; i <= 10; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(30);
            RowConstraints rowConstraints = new RowConstraints(30);
            board.getColumnConstraints().add(columnConstraints);
            board.getRowConstraints().add(rowConstraints);
        }

        for (int col = 1; col <= 10; col++) {
            Label label = new Label(String.valueOf((char) ('A' + col - 1)));
            label.setMinSize(30, 30);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-weight: bold; -fx-background-color: #e0e0e0; -fx-font-size: 14px; -fx-border-color: #b0b0b0;");
            board.add(label, col, 0);
        }

        for (int row = 1; row <= 10; row++) {
            Label label = new Label(String.valueOf(row));
            label.setMinSize(30, 30);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-weight: bold; -fx-background-color: #e0e0e0; -fx-font-size: 14px; -fx-border-color: #b0b0b0;");
            board.add(label, 0, row);
        }

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(30, 30);
                cell.setMinSize(30, 30);
                cell.setMaxSize(30, 30);
                Button btn = new Button();
                btn.setPrefSize(30, 30);
                btn.setMinSize(30, 30);
                btn.setMaxSize(30, 30);
                btn.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
                btn.getStyleClass().add("grid-button");
                cell.getChildren().add(btn);
                cell.getStyleClass().add("grid-button");
                int finalRow = row;
                int finalCol = col;
                btn.setOnMouseClicked(e -> {
                    if (!finishedPlacing && isPlayer) {
                        System.out.println("Colocando barco del jugador en la posición " + finalRow + "," + finalCol);
                        placePlayerShip(finalRow, finalCol);
                    } else if (finishedPlacing && !isPlayer && e.getButton() == MouseButton.PRIMARY) {
                        System.out.printf("Disparando en la posición [%d%c], es decir (%d,%d)\n", finalRow + 1, (char)('A' + finalCol), finalRow,  finalCol);
                        try {
                            handlePlayerShot(finalRow, finalCol);
                        } catch (DoubleShootException ex) {
                            System.out.println(ex.getMessage() + " Intenta disparar en otra.");
                            String message = String.format("Ya disparaste en la casilla [%d%c].\nIntenta en otra.", finalRow + 1, (char)('A' + finalCol));
                            UIVisualHelper.showTemporaryLabel(errorLabel, message);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                board.add(cell, col + 1, row + 1);
            }
        }
        board.setHgap(0);
        board.setVgap(0);
        board.setSnapToPixel(true);
    }

    /**
     * Toggles the orientation of the ship to be placed (horizontal or vertical),
     * updates the text of the toggle button, and redraws the currently selected ship canvas accordingly.
     */
    private void toggleOrientation() {
        if (orientationToggle.isSelected()) {
            orientationToggle.setText("Horizontal");
        } else {
            orientationToggle.setText("Vertical");
        }

        if (selectedShipCanvas != null) {
            boolean horizontal = !orientationToggle.isSelected();

            double width = horizontal ? selectedShipSize * 30 : 30;
            double height = horizontal ? 30 : selectedShipSize * 30;
            selectedShipCanvas.setWidth(width);
            selectedShipCanvas.setHeight(height);

            GraphicsContext gc = selectedShipCanvas.getGraphicsContext2D();
            drawShipOnCanvas(gc, horizontal, selectedShipSize);
        }
    }

    /**
     * Initializes the canvas elements inside the ship selector container,
     * displaying each ship size according to the configured fleet composition.
     * It also sets the click handlers for selecting a ship canvas.
     */
    private void initializeShipSelectorCanvases() {
        shipSelectorContainer.getChildren().clear();
        shipRowMap.clear();

        List<Integer> sortedSizes = new ArrayList<>(fleetComposition.keySet());
        Collections.sort(sortedSizes);

        for (int size : sortedSizes) {
            int quantity = fleetComposition.get(size);

            HBox shipRow = new HBox(5);
            shipRow.setAlignment(Pos.CENTER_LEFT);
            shipRow.setPadding(new Insets(5));

            for (int i = 0; i < quantity; i++) {
                Canvas shipCanvas = new Canvas(30 * size, 30);
                GraphicsContext gc = shipCanvas.getGraphicsContext2D();

                Image shipImage = shipImages.get(size);
                if (shipImage != null) {
                    gc.drawImage(shipImage, 0, 0, 30 * size, 30);
                }
                shipCanvas.setOnMouseClicked(_ -> selectShipCanvas(shipCanvas, size));
                shipRow.getChildren().add(shipCanvas);
            }
            shipRowMap.put(size, shipRow);
            shipSelectorContainer.getChildren().add(shipRow);
        }
        selectedShipCanvas = null;
        selectedShipSize = 0;
    }

    /**
     * Draws a ship onto the specified canvas graphics context based on the given orientation and size.
     *
     * @param gc the {@link GraphicsContext} of the canvas where the ship will be drawn.
     * @param horizontal True if the ship is horizontal; false for vertical.
     * @param size the size of the ship to draw.
     */
    private void drawShipOnCanvas(GraphicsContext gc, boolean horizontal, int size) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        Image boatImage = shipImages.get(size);

        if (boatImage == null) {
            System.out.println("No se encontró imagen para tamaño " + size);
            return;
        }

        if(size == 1) {
            double imgWidth = boatImage.getWidth();
            double imgHeight = boatImage.getHeight();
            double canvasSize = 30;
            double scale = Math.min(canvasSize / imgWidth, canvasSize / imgHeight);
            double drawWidth = imgWidth * scale;
            double drawHeight = imgHeight * scale;
            double offsetX = (canvasSize - drawWidth) / 2;
            double offsetY = (canvasSize - drawHeight) / 2;
            gc.save();

            if(!horizontal) {
                gc.translate(canvasSize / 2, canvasSize / 2);
                gc.rotate(-90);
                gc.translate(-canvasSize / 2, -canvasSize / 2);
            }

            gc.drawImage(boatImage, offsetX, offsetY, drawWidth, drawHeight);
            gc.restore();
            return;
        }

        for (int i = 0; i < size; i++) {
            boolean isFirst = (i == 0);
            boolean isLast = (i == size - 1);

            double x = horizontal ? i * 30 : 0;
            double y = horizontal ? 0 : i * 30;

            gc.save();

            if (!horizontal) {
                gc.translate(x + 30, y);
                gc.rotate(90);
                x = 0;
                y = 0;
            }
            drawBoatShapeSegment(gc, isFirst, isLast, size, boatImage, x, y);
            gc.restore();
        }
    }

    /**
     * Draws a single segment of a boat (either head, body, or tail) on the canvas.
     *
     * @param gc the {@link GraphicsContext} for drawing.
     * @param isFirst True if the segment is the first (front of the ship).
     * @param isLast  True if the segment is the last (rear of the ship).
     * @param shipLength the total length of the ship.
     * @param boatImage the image representing the boat.
     * @param x the X (horizontal) position to draw the segment.
     * @param y the Y (vertical) position to draw the segment.
     */
    private void drawBoatShapeSegment(GraphicsContext gc, boolean isFirst, boolean isLast, int shipLength, Image boatImage, double x, double y) {
        if(shipLength == 1) {
            gc.drawImage(boatImage, x, y);
            return;
        }
        double boatWidth = boatImage.getWidth();
        double boatHeight = boatImage.getHeight();
        double segmentWidth = boatWidth / 3;

        double inset = (shipLength == 4) ? 0.2 : 0.0;
        double destSize = 30 - 2 * inset;

        if (isFirst) {
            gc.drawImage(boatImage, 0, 0, segmentWidth, boatHeight, x + inset, y + inset, destSize, destSize);
        } else if (isLast) {
            gc.drawImage(boatImage, 2 * segmentWidth, 0, segmentWidth, boatHeight, x + inset, y + inset, destSize, destSize);
        } else {
            gc.drawImage(boatImage, segmentWidth, 0, segmentWidth, boatHeight, x + inset, y + inset, destSize, destSize);
        }
    }

    /**
     * Replaces the previously selected ship canvas with a new, semi-transparent canvas
     * indicating the selected ship size and orientation. Updates internal selection tracking.
     *
     * @param oldCanvas the canvas that was previously displayed and selected.
     * @param size the size of the ship to select.
     */
    private void selectShipCanvas(Canvas oldCanvas, int size) {
        if (selectedShipCanvas != null) {
            selectedShipCanvas.setOpacity(1.0);
        }
        boolean horizontal = !orientationToggle.isSelected();
        double canvasWidth = horizontal ? size * 30 : 30;
        double canvasHeight = horizontal ? 30 : size * 30;

        Canvas newCanvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = newCanvas.getGraphicsContext2D();
        drawShipOnCanvas(gc, horizontal, size);
        newCanvas.setOpacity(0.6);

        if (oldCanvas.getParent() instanceof Pane parent) {
            int index = parent.getChildrenUnmodifiable().indexOf(oldCanvas);
            if (index != -1) {
                parent.getChildren().set(index, newCanvas);
            }
        }

        selectedShipCanvas = newCanvas;
        selectedShipSize = size;
    }

    /**
     * Attempts to place a ship on the player's board at the specified position.
     * Handles constraints such as maximum ships per size, overlapping validation,
     * and updates the visual representation. Once all ships are placed,
     * it enables difficulty selection for the enemy AI.
     *
     * @param row the row index to place the ship's starting coordinate.
     * @param col the column index to place the ship's starting coordinate.
     * @see UIVisualHelper
     */
    private void placePlayerShip(int row, int col) {
        if (selectedShipCanvas == null) {
            System.out.println("No hay barco seleccionado para colocar.");
            return;
        }

        int size = selectedShipSize;

        int placed = placedShipsCount.getOrDefault(size, 0);
        int maxAllowed = fleetComposition.getOrDefault(size, 0);
        if (placed >= maxAllowed) {
            System.out.println("Ya colocaste todos los barcos de tamaño " + size);
            return;
        }

        boolean horizontal = !orientationToggle.isSelected();

        try {
            Ship ship = playerBoardModel.placeShip(row, col, size, horizontal, true);
            if (ship != null) {
                playerShips.add(ship);

                drawShip(playerBoard, ship, true);

                placedShipsCount.put(size, placed + 1);

                HBox shipRow = shipRowMap.get(size);
                if (shipRow != null) {
                    shipRow.getChildren().remove(selectedShipCanvas);
                    if (shipRow.getChildren().isEmpty()) {
                        shipSelectorContainer.getChildren().remove(shipRow);
                        shipRowMap.remove(size);
                    }
                }
                selectedShipCanvas = null;
                selectedShipSize = 0;

                boolean hasRemainingShips = shipSelectorContainer.getChildren().stream()
                        .anyMatch(node -> node instanceof HBox && !((HBox) node).getChildren().isEmpty());

                if (!hasRemainingShips) {
                    configLabel.setVisible(false);
                    orientationToggle.setDisable(true);
                    orientationToggle.setVisible(false);
                    orientationToggle.setManaged(false);
                    monitorButton.setDisable(false);
                    difficultyLabel.setVisible(true);
                    difficultyLabel.setManaged(true);
                    difficultySelector.setVisible(true);
                    difficultySelector.setManaged(true);
                    difficultySelector.getItems().addAll("Fácil", "Difícil");
                    difficultySelector.setOnAction(_ -> {
                        String difficulty = difficultySelector.getValue();
                        difficultySelector.setDisable(true);
                        selectedDifficulty = difficulty;
                        enemy.setDifficulty(selectedDifficulty);
                        System.out.println("Dificultad seleccionada: " + selectedDifficulty);
                        difficultyLabel.setVisible(false);
                        difficultyLabel.setManaged(false);

                    });
                }
            } else {
                System.out.println("No se pudo colocar el barco en esa posición.");
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException | IllegalStateException e ) {
            System.out.println("Error: " + e.getMessage());
            UIVisualHelper.showTemporaryLabel(exceptLabel, "¡Cuidado!\n" + e.getMessage());
        } finally {
            saveGameState();
        }
    }

    /**
     * Visually draws a ship on the given game board by adding canvas segments to the grid.
     * Handles orientation and whether it's the player's ship or the machine's.
     *
     * @param board the board {@link GridPane} where the ship should be drawn.
     * @param ship the {@link Ship} instance to render.
     * @param playerShip True if the ship belongs to the player; false for machine ships.
     */
    private void drawShip(GridPane board, Ship ship, boolean playerShip) {
        Image boatImage = shipImages.get(ship.getSize());

        if (boatImage == null) {
            System.out.println("No se encontró imagen para tamaño " + ship.getSize());
            return;
        }

        List<Ship.Coordinate> coords = ship.getCoordinates();
        for (int i = 0; i < coords.size(); i++) {
            Ship.Coordinate coord = coords.get(i);
            int row = coord.getRow();
            int col = coord.getCol();
            boolean isFirst = (i == 0);
            boolean isLast = (i == coords.size() - 1);
            StackPane cell = getStackPaneAt(board, row, col);
            if (cell != null) {
                Canvas canvas = new Canvas(30, 30);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                drawBoatShape(gc, ship.isHorizontal(), isFirst, isLast, ship.getSize(), boatImage);
                canvas.setMouseTransparent(true);
                canvas.setManaged(false);
                if (!playerShip) {
                    canvas.setVisible(monitorMode);
                    canvas.setUserData("enemy");
                }
                StackPane.setAlignment(canvas, Pos.CENTER);
                cell.getChildren().add(canvas);
            }
        }
    }

    /**
     * Retrieves the StackPane at the specified logical board coordinates (row, col).
     *
     * @param board The GridPane board.
     * @param row The row index of the desired cell.
     * @param col The column index of the desired cell.
     * @return The StackPane at the specified coordinates, or null if not found.
     */
    public StackPane getStackPaneAt(GridPane board, int row, int col) {
        int visualRow = row + 1;
        int visualCol = col + 1;

        for (javafx.scene.Node node : board.getChildren()) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeCol = GridPane.getColumnIndex(node);
            if (nodeRow != null && nodeCol != null && nodeRow == visualRow && nodeCol == visualCol) {
                return (StackPane) node;
            }
        }
        return null;
    }

    /**
     * Handles the player's shot action on the enemy's board.
     * Updates the UI, evaluates the result, and checks for win conditions.
     *
     * @param row The row where the player fires.
     * @param col The column where the player fires.
     * @throws DoubleShootException If the player attempts to shoot the same cell more than once.
     * @throws IOException If the writing player data to the text file fail.
     * @see com.example.miniproyecto3.exceptions.DoubleShootException
     */
    private void handlePlayerShot(int row, int col) throws DoubleShootException, IOException{
        if (!playerTurn || gameEnded) return;

        boolean hit = player.makeMove(row, col, enemyBoardModel, enemyShips);

        StackPane cell = getStackPaneAt(enemyBoard, row, col);
        Canvas canvas = new Canvas(30, 30);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setMouseTransparent(true);
        canvas.setManaged(false);
        canvas.setUserData(hit ? "impacto" : "fallo");
        drawShot(gc, hit, false);
        cell.getChildren().add(canvas);

        if(hit) {
            Ship ship = getShipAt(enemyShips, row, col);
            if(ship != null && ship.isSunk()) {
                System.out.println("Hundiste un barco.");
                drawSunkShips(ship, enemyBoard);
                labelScore.setText("Tu puntaje: " + player.getPlayerScore());
            }
            checkWinCondition();
        } else {
            endPlayerTurn();
        }
        debugBoards();
        saveGameState();
        planeTextFileHandler.write("PlayerData.csv",player.getPlayerName() + "," + player.getPlayerScore());
    }

    /**
     * Triggers the machine's move based on the current difficulty setting.
     * Executes the enemy shot, updates the board, and evaluates the hit.
     */
    private void triggerComputerMove() {
        playerTurn = false;
        boolean hit;

        if (enemy.getDifficulty() == 1) {
            hit = enemy.makeRandomMove(playerBoardModel, playerShips);
        } else {
            hit = enemy.makeMove(0, 0, playerBoardModel, playerShips);
        }

        int lastRow = enemy.getLastShotRow();
        int lastCol = enemy.getLastShotCol();

        StackPane cell = getStackPaneAt(playerBoard, lastRow, lastCol);
        Canvas canvas = new Canvas(30, 30);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setMouseTransparent(true);
        canvas.setUserData(hit ? "impacto" : "fallo");
        drawShot(gc, hit, false);
        cell.getChildren().add(canvas);

        if(hit) {
            Ship ship = getShipAt(playerShips, lastRow, lastCol);
            if(ship != null && ship.isSunk()) {
                drawSunkShips(ship, playerBoard);
            }

            checkWinCondition();

            if(!gameEnded) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> triggerComputerMove());
                    }
                }, 1000);
            }
        } else {
            endComputerTurn();
        }
        debugBoards();
        saveGameState();
    }

    /**
     * Ends the computer's turn and switches control back to the player.
     * Also checks for any win condition.
     */
    private void endComputerTurn() {
        playerTurn = true;
        checkWinCondition();
    }

    /**
     * Ends the player's turn and schedules the computer's response move with delay.
     */
    private void endPlayerTurn(){
        playerTurn = false;
        checkWinCondition();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> triggerComputerMove());
            }}, 1000);
    }

    /**
     * Retrieves the ship occupying the given coordinates, if any.
     *
     * @param ships The list of ships to search.
     * @param row The row coordinate.
     * @param col The column coordinate.
     * @return The {@link Ship} that occupies the given coordinates, or null if none found.
     * @see Ship
     */
    public Ship getShipAt(List<Ship> ships, int row, int col) {
        for (Ship ship : ships) {
            if (ship.occupies(row, col)) {
                return ship;
            }
        }
        return null;
    }

    /**
     * Checks whether the game has reached a win condition (either player or machine wins).
     * If the condition is met, displays the corresponding alert, stops the music,
     * deletes the saved game file, and exits the application.
     *
     * @see UIVisualHelper
     */
    public void checkWinCondition() {
        if (gameEnded) return;

        boolean allEnemySunk = enemyShips.stream().allMatch(Ship::isSunk);
        boolean allPlayerSunk = playerShips.stream().allMatch(Ship::isSunk);

        if (allEnemySunk) {
            gameEnded = true;
            GameStage.deleteInstance();
            UIVisualHelper.showGameAlert("VICTORIA", "¡Ganaste! Has hundido todos los barcos enemigos.");
            playerTurn = false;
            System.out.println("Eliminando la partida...");
            File file = new File("GameState.ser");
            if(!file.delete()) {
                System.out.println("Advertencia: No se pudo eliminar GameState.ser después de ganar la partida.");
            }
            continueGame = false;
            musicPlayer.stop();
            Platform.exit();
            System.exit(0);

        } else if (allPlayerSunk) {
            gameEnded = true;
            GameStage.deleteInstance();
            UIVisualHelper.showGameAlert("DERROTA", "¡Has perdido! La máquina ha hundido todos tus barcos.");
            playerTurn = false;
            System.out.println("Eliminando la partida...");
            File file = new File("GameState.ser");
            if(!file.delete()) {
                System.out.println("Advertencia: No se pudo eliminar GameState.ser después de perder.");
            }
            continueGame = false;
            musicPlayer.stop();
            Platform.exit();
            System.exit(0);
        }
    }

    /**
     * Handles the finalization of the player's ship placement.
     * Starts the machine ship placement and sets up the game for battle.
     * If ships are missing, prompts the user with a warning alert.
     */
    @FXML
    private void handleFinishPlacement() {
        if (shipSelectorContainer.getChildren().isEmpty()) {
            musicPlayer.stop();
            musicPlayer = new MusicPlayer("/com/example/miniproyecto3/Media/RedAlert3Theme.mp3");
            musicPlayer.play();
            finishedPlacing = true;
            placeEnemyShips();
            readyButton.setDisable(finishedPlacing);
            saveGameState();
            placementControls.setVisible(false);
            placementControls.setManaged(false);
            enemyBoardContainer.setVisible(true);
            enemyBoardContainer.setManaged(true);
            musicPlayer = new MusicPlayer("/com/example/miniproyecto3/Media/RedAlert3Theme.mp3");
            musicPlayer.play();
            monitorButton.setVisible(true);
            monitorButton.setManaged(true);
            labelScore.setVisible(true);
            System.out.println("Debug para score: " + player.getPlayerScore());
            labelScore.setText("Tu puntaje: " + player.getPlayerScore());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Coloca todos los barcos antes de continuar.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Places the enemy ships on the board randomly using predefined fleet sizes.
     * Ships are placed only if they do not overlap or go out of bounds.
     * Any placement errors are logged, and the method retries until placement is valid.
     */
    private void placeEnemyShips() {
        Random rand = new Random();
        List<Integer> shipSizes = Arrays.asList(1, 2, 3, 4);

        Map<Integer, Integer> fleetCount = Map.of(
                1, 4,
                2, 3,
                3, 2,
                4, 1
        );

        int lastSize = shipSizes.getLast();
        int lastIndex = fleetCount.get(lastSize) - 1;

        for (int size : shipSizes) {
            int count = fleetCount.getOrDefault(size, 1);
            for (int i = 0; i < count; i++) {
                boolean placed = false;
                while (!placed) {
                    int row = rand.nextInt(10);
                    int col = rand.nextInt(10);
                    boolean horizontal = rand.nextBoolean();

                    try {
                        Ship ship = enemyBoardModel.placeShip(row, col, size, horizontal, false);
                        if (ship != null) {
                            enemyShips.add(ship);
                            drawShip(enemyBoard, ship, false);
                            placed = true;
                        }
                    } catch (IllegalArgumentException | IndexOutOfBoundsException | IllegalStateException e) {
                        System.out.println("Error de la máquina: " +  e.getMessage() + " Volviendo a ubicar el barco.");
                    } finally {
                        if(placed && size == lastSize && i == lastIndex) {
                            System.out.println("Todos los barcos de la IA fueron colocados correctamente.");
                        }
                    }
                }
            }
        }
        debugBoards();
    }


    /**
     * Toggles the visibility of the enemy ships on the board.
     * Changes the label of the toggle button accordingly.
     */
    @FXML
    private void toggleEnemyBoard() {
        monitorMode = !monitorMode;
        for (javafx.scene.Node node : enemyBoard.getChildren()) {
            if (node instanceof StackPane cell) {
                for (javafx.scene.Node child : cell.getChildren()) {
                    if (child instanceof Canvas canvas && "enemy".equals(canvas.getUserData())) {
                        canvas.setVisible(monitorMode);
                    }
                }
            }
        }

        if (monitorMode) {
            monitorButton.setText("Ocultar embarcaciones enemigas");
            System.out.println("Modo monitor activado.");
        } else {
            monitorButton.setText("Mostrar embarcaciones enemigas");
        }
    }

    /**
     * Draws a specific segment of a ship image on a canvas cell depending on its position and orientation.
     *
     * @param gc the GraphicsContext used to draw on the canvas.
     * @param horizontal true if the ship is placed horizontally, false if vertically.
     * @param isFirst true if the segment is the first part of the ship.
     * @param isLast true if the segment is the last part of the ship.
     * @param shipLength the total length of the ship.
     * @param boatImage the image used to represent the ship.
     */
    private void drawBoatShape(GraphicsContext gc, boolean horizontal, boolean isFirst, boolean isLast, int shipLength, Image boatImage) {
        gc.clearRect(0, 0, 30, 30);

        if(shipLength == 1) {
            gc.save();

            double cellSize = 30;
            double scaleFactor = 0.9;
            double drawSize = cellSize * scaleFactor;
            double offset = (cellSize - drawSize) / 2;

            if(!horizontal) {
                gc.translate(0, cellSize);
                gc.rotate(-90);

                gc.drawImage(boatImage, offset, offset, drawSize, drawSize);
            } else {
                gc.drawImage(boatImage, offset, offset, drawSize, drawSize);
            }

            gc.restore();
            return;
        }

        double boatWidth = boatImage.getWidth();
        double boatHeight = boatImage.getHeight();
        double segmentWidth = boatWidth / 3;

        double inset = (shipLength == 4) ? 0.2 : 0.0;
        double destSize = 30 - 2 * inset;

        if (horizontal) {
            if (isFirst) {
                gc.drawImage(boatImage, 0, 0, segmentWidth, boatHeight, inset, inset, destSize, destSize);
            } else if (isLast) {
                gc.drawImage(boatImage, 2 * segmentWidth, 0, segmentWidth, boatHeight, inset, inset, destSize, destSize);
            } else {
                gc.drawImage(boatImage, segmentWidth, 0, segmentWidth, boatHeight, inset, inset, destSize, destSize);
            }
        } else {
            gc.save();
            gc.rotate(90);

            if (isFirst) {
                gc.drawImage(boatImage, 0, 0, segmentWidth, boatHeight, inset, inset - 30, destSize, destSize);
            } else if (isLast) {
                gc.drawImage(boatImage, 2 * segmentWidth, 0, segmentWidth, boatHeight, inset, inset - 30, destSize, destSize);
            } else {
                gc.drawImage(boatImage, segmentWidth, 0, segmentWidth, boatHeight, inset, inset - 30, destSize, destSize);
            }

            gc.restore();
        }
    }

    /**
     * Prints a debug representation of both the player and machine boards to the console,
     * including ship positions, hits, and sunk ship status.
     */
    public void debugBoards() {
        System.out.println("=== DEBUG: playerBoardModel                    enemyBoardModel  ===");

        ArrayList<ArrayList<Boolean>> enemyGrid = enemyBoardModel.getEnemyBoard();
        ArrayList<ArrayList<Boolean>> playerGrid = playerBoardModel.getPlayerBoard();
        ArrayList<ArrayList<Boolean>> enemyShots = enemyBoardModel.getShotsOnEnemyBoard();
        ArrayList<ArrayList<Boolean>> playerShots = playerBoardModel.getShotsOnPlayerBoard();
        ArrayList<ArrayList<Boolean>> sunkEnemy = enemyBoardModel.getSunkEnemyShips();
        ArrayList<ArrayList<Boolean>> sunkPlayer = playerBoardModel.getSunkPlayerShips();

        int size = enemyGrid.size();

        String spacing = "    ";
        System.out.print(spacing);
        for (int col = 0; col < size; col++) {
            System.out.printf(" %c ", 'A' + col);
        }
        System.out.print("   ");
        System.out.print(spacing);
        for (int col = 0; col < size; col++) {
            System.out.printf(" %c ", 'A' + col);
        }
        System.out.println();

        for (int row = 0; row < size; row++) {
            String playerRow = buildDebugRow(playerGrid, playerShots, sunkPlayer, row);
            String enemyRow = buildDebugRow(enemyGrid, enemyShots, sunkEnemy, row);
            System.out.printf("%2d  %s   %2d  %s%n", row + 1, playerRow, row + 1, enemyRow);
        }
    }

    /**
     * Constructs a formatted string representing a single row of the debug board,
     * showing ship positions, hits, and sunk status.
     *
     * @param grid the grid indicating where ships are placed.
     * @param shots the grid of cells that have been shot at.
     * @param sunk the grid indicating which ships have been sunk.
     * @param row the index of the row to build.
     * @return a formatted string representing that row.
     */
    private String buildDebugRow(ArrayList<ArrayList<Boolean>> grid, ArrayList<ArrayList<Boolean>> shots, ArrayList<ArrayList<Boolean>> sunk, int row) {
        StringBuilder result = new StringBuilder();
        int size = grid.size();

        for(int col = 0; col < size; col++) {
            if(sunk.get(row).get(col)) {
                result.append("[ ]");
            } else if(grid.get(row).get(col)) {
                if(shots.get(row).get(col)) {
                    result.append("[*]");
                } else {
                    result.append("[X]");
                }
            } else {
                if(shots.get(row).get(col)) {
                    result.append("[~]");
                } else {
                    result.append("[ ]");
                }
            }
        }

        return result.toString();
    }

    /**
     * Saves the current state of the game to a serialized file.
     * The game state includes both boards, ships, and machine AI.
     */
    public void saveGameState() {
        GameState state = new GameState(playerBoardModel, enemyBoardModel, playerShips, enemyShips, enemy);
        SerializableFileHandler handler = new SerializableFileHandler();
        handler.serialize("GameState.ser", state);
        System.out.println("Estado del juego guardado con éxito en: GameState.ser");
    }

    /**
     * Loads the game state from a previously saved serialized file.
     * Updates both logical and graphical representations of the game.
     *
     * @throws IOException if there is an error during file reading or UI board creation.
     */
    public void loadGameState() throws IOException {
        SerializableFileHandler handler = new SerializableFileHandler();
        GameState state = (GameState) handler.deserialize("GameState.ser");
        if (state != null) {
            this.playerBoardModel = state.getPlayerBoard();
            this.enemyBoardModel = state.getEnemyBoard();
            this.playerShips = state.getPlayerShips();
            this.enemyShips = state.getEnemyShips();
            this.enemy = state.getEnemy();
            redrawBoards();
        }
    }

    /**
     * Restores all the shots (hits and misses) on a given board after loading a saved game.
     *
     * @param boardModel The logical board model to determine shot positions.
     * @param board The visual GridPane where the shots will be rendered.
     * @param isPlayerBoard True if restoring on player's board, false for machine's board.
     */
    private void restoreShots(Board boardModel, GridPane board, boolean isPlayerBoard) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (!boardModel.alreadyShotAt(row, col, isPlayerBoard)) continue;

                StackPane cell = getStackPaneAt(board, row, col);
                if (cell == null) continue;

                boolean skip = false;
                for (Node node : cell.getChildren()) {
                    if ("hundido".equals(node.getUserData())) {
                        skip = true;
                        break;
                    }
                }
                if (skip) continue;

                Canvas canvas = new Canvas(30, 30);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                canvas.setMouseTransparent(true);
                canvas.setManaged(false);

                boolean hit = boardModel.hasShipAt(row, col, !isPlayerBoard);
                drawShot(gc, hit, false);

                canvas.setUserData(hit ? "impacto" : "fallo");
                cell.getChildren().add(canvas);
            }
        }
    }

    /**
     * Reconstructs and redraws both the player’s and the machine’s boards visually and logically.
     * Includes ships, sunk states, and restored shots.
     *
     * @throws IOException if board creation fails due to I/O errors.
     */
    private void redrawBoards() throws IOException {
        createBoard(playerBoard, true);
        createBoard(enemyBoard, false);

        for (Ship ship : playerShips) {
            drawShip(playerBoard, ship, true);
            if (ship.isSunk()) {
                drawSunkShips(ship, playerBoard);
            }
        }

        for (Ship ship : enemyShips) {
            drawShip(enemyBoard, ship, false);
            if (ship.isSunk()) {
                drawSunkShips(ship, enemyBoard);
            }
        }

        restoreShots(enemyBoardModel, enemyBoard, true);
        restoreShots(playerBoardModel, playerBoard, false);
    }

    /**
     * Draws a visual representation of a shot on the canvas.
     * The image displayed depends on whether the shot was a hit, a sunk ship, or a miss.
     *
     * @param gc The GraphicsContext used to draw the image.
     * @param isHit True if the shot hit a ship.
     * @param isSunk True if the shot sunk a ship.
     */
    public void drawShot(GraphicsContext gc, boolean isHit, boolean isSunk) {
        gc.clearRect(0, 0, 30, 30);
        Image imgToDraw = isSunk ? smoke : (isHit ? explosion : miss);
        gc.drawImage(imgToDraw, 0, 0, 30, 30);
        gc.restore();
    }

    /**
     * Draws visual indicators for all coordinates of a sunk ship on the game board.
     * Replaces previous "hit" markers with special visuals indicating that the ship has been destroyed.
     *
     * @param ship The Ship object that has been sunk.
     * @param board The GridPane on which the ship is drawn (either player's or machine's board).
     */
    public void drawSunkShips(Ship ship, GridPane board) {
        List<Ship.Coordinate> coords = ship.getCoordinates();
        for (Ship.Coordinate coordinate : coords) {
            int row = coordinate.getRow();
            int col = coordinate.getCol();
            StackPane cell = getStackPaneAt(board, row, col);
            if (cell != null) {
                cell.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().equals("impacto"));
                Canvas canvas = new Canvas(30, 30);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                drawShot(gc, false, true);
                canvas.setMouseTransparent(true);
                canvas.setManaged(false);
                canvas.setUserData("hundido");
                StackPane.setAlignment(canvas, Pos.CENTER);
                cell.getChildren().add(canvas);
            }
        }
    }
}