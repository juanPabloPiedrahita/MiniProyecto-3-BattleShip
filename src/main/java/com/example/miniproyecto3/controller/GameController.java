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

public class GameController {

    //Objetos del Fxml (visual)
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


    //Objetos para llevar la logica interna del juego
    private Board playerBoardModel = new Board();
    private Board enemyBoardModel = new Board();
    private List<Ship> playerShips = new ArrayList<>();
    private List<Ship> enemyShips = new ArrayList<>();

    //atributos para llevar monitoreo constante del estado del juego
    private boolean finishedPlacing = false;
    private boolean monitorMode = false;
    private boolean playerTurn = true;
    private boolean gameEnded = false;

    //planeTextFileHandler
    private PlaneTextFileHandler planeTextFileHandler;

    //player
    private Player player;

    //variable para saber si le dio a continuar
    private boolean continueGame;

    //esto ayudara a la IA a tener memoria a la hora de disparar
    private String selectedDifficulty;  // Para almacenar la dificultad seleccionada.

    private Image explosion;
    private Image miss;
    private Image smoke;

    //Objeto para reproducir musica
    MusicPlayer musicPlayer;

    //Para mejora visual.
    private Canvas selectedShipCanvas = null;
    private int selectedShipSize = 0;

    //Para configurar la flota como pide el enunciado.
    private final Map<Integer, Integer> fleetComposition = Map.of(
            1, 4,  //4 fragatas de 1 casilla
            2, 3,     //3 destructores de 2 casillas
            3, 2,    //2 submarinos de 3 casillas
            4, 1        //1 portaaviones de 4 casillas
    );
    private Map<Integer, Image> shipImages; //Para almacenar las imágenes de los barcos.
    private final Map<Integer, Integer> placedShipsCount = new HashMap<>();
    private final Map<Integer, HBox> shipRowMap = new HashMap<>();

    private AI enemy = new AI(0,"Enemy", "Fácil");

    @FXML
    public void initialize() {//Esta funcion es el punto de partida de la ventana GameStage, cualquier Fmxl tiene una de estas y se llama automaticamente al abrir una instancia de GameStage
        try {
                enemyBoard.getStyleClass().add("grid-pane");
                playerBoard.getStyleClass().add("grid-pane");
                label1.getStyleClass().add("enemy-turn-label");
                musicPlayer = new MusicPlayer("/com/example/miniproyecto3/Media/SelectionTheme.mp3");
                musicPlayer.play();
                planeTextFileHandler = new PlaneTextFileHandler();
                player = WelcomeStage.getInstance().getWelController().getPlayer();
                System.out.println("Player: " + player.getPlayerName() + ", " + player.getPlayerScore());
                continueGame = WelcomeStage.getInstance().getWelController().getContinue();
                WelcomeStage.deleteInstance();
                smoke = loadImageOrThrow("/com/example/miniproyecto3/Image/blackSmoke23.png");
                miss = loadImageOrThrow("/com/example/miniproyecto3/Image/waterExplosion.png");
                explosion = loadImageOrThrow("/com/example/miniproyecto3/Image/explosion08.png");
                shipImages = new HashMap<>();
                shipImages.put(1, loadImageOrThrow("/com/example/miniproyecto3/Image/prueba8.png"));
                shipImages.put(2, loadImageOrThrow("/com/example/miniproyecto3/Image/prueba.png"));
                shipImages.put(3, loadImageOrThrow("/com/example/miniproyecto3/Image/prueba4.png"));
                shipImages.put(4, loadImageOrThrow("/com/example/miniproyecto3/Image/prueba2.png"));
                if (!continueGame) { //Si el jugador le dio a jugar (no continuar) el juego crea una nueva partida desde 0
                    System.out.println("Nuevo juego...");
                    System.out.println("Creando playerboard");
                    createBoard(playerBoard, true);
                    System.out.println("Creando enemyboard");
                    createBoard(enemyBoard, false);
                    System.out.println("seleccionando primera opcion en shipSizeSelector");
                    System.out.println("Creando evento para orientationToggle");
                    orientationToggle.setOnAction(_ -> toggleOrientation());
                    System.out.println("Desactivando monitorMode");
                    monitorButton.setDisable(true);
                    placementControls.setVisible(true);
                    placementControls.setManaged(true);
                    enemyBoardContainer.setVisible(false);
                    enemyBoardContainer.setManaged(false);
                    monitorButton.setVisible(false);
                    monitorButton.setManaged(false);
                    initializeShipSelectorCanvases();
                } else { //Si el jugador le dio a continuar carga la partida mas reciente :v
                    System.out.println("Entrando a cargar el juego mas reciente");
                    finishedPlacing = true;
                    loadGameState();
                    readyButton.setDisable(true);
                    orientationToggle.setDisable(true);
                    placementControls.setVisible(false);
                    placementControls.setManaged(false);
                    enemyBoardContainer.setVisible(true);
                    enemyBoardContainer.setManaged(true);
                    monitorButton.setVisible(true);
                    monitorButton.setManaged(true);
                }

        } catch (IOException e) {
            System.out.println("Error cargando la imagen." + e.getMessage());
        } catch (VisualException ex) {
            System.out.println("Ocurrió un error al cargar el juego." + ex.getMessage());
        }
    }

    // método utilitario con IOException para las imágenes.
    private Image loadImageOrThrow(String path) throws IOException {
        URL resourceUrl = getClass().getResource(path);
        if(resourceUrl == null) {
            throw new IOException("No se pudo encontrar el recurso: " + path);
        }

        return new Image(resourceUrl.toExternalForm());
    }

    //Este metodo crea los gridpanes (tableros visuales) de amboss jugadores, tanto jugador como maquina
    private void createBoard(GridPane board, boolean isPlayer) {
        System.out.println("Creando " + isPlayer + " board (gridpane)");
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
                System.out.println("Creando  + isPlayer +  board (gridpane). Coords: )" + row + "," + col);
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
                        System.out.println("poniendo barco del jugador en la posicion " + finalRow + "," + finalCol);
                        placePlayerShip(finalRow, finalCol);
                    } else if (finishedPlacing && !isPlayer && e.getButton() == MouseButton.PRIMARY) {
                        System.out.printf("Disparando en la posicion [%d%c], es decir (%d,%d)\n", finalRow + 1, (char)('A' + finalCol), finalRow,  finalCol);
                        try {
                            handlePlayerShot(finalRow, finalCol);
                        } catch (DoubleShootException ex) {
                            System.out.println(ex.getMessage() + " Intenta disparar en otra.");
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

    private void toggleOrientation() {
        if (orientationToggle.isSelected()) {
            orientationToggle.setText("Horizontal");
        } else {
            orientationToggle.setText("Vertical");
        }

        if (selectedShipCanvas != null) {
            boolean horizontal = !orientationToggle.isSelected();

            // Ajustar el tamaño del canvas según la orientación
            double width = horizontal ? selectedShipSize * 30 : 30;
            double height = horizontal ? 30 : selectedShipSize * 30;
            selectedShipCanvas.setWidth(width);
            selectedShipCanvas.setHeight(height);

            // Redibujar el barco con la nueva orientación
            GraphicsContext gc = selectedShipCanvas.getGraphicsContext2D();
            drawShipOnCanvas(gc, horizontal, selectedShipSize);
        }
    }


    private void initializeShipSelectorCanvases() {
        shipSelectorContainer.getChildren().clear();
        shipRowMap.clear();

        List<Integer> sortedSizes = new ArrayList<>(fleetComposition.keySet());
        Collections.sort(sortedSizes);  // Fragatas (1)  ->  Portaaviones (4).

        for (int size : sortedSizes) {
            int quantity = fleetComposition.get(size);

            HBox shipRow = new HBox(5);  //Espaciado para barcos del mismo tipo.
            shipRow.setAlignment(Pos.CENTER_LEFT);
            shipRow.setPadding(new Insets(5));  //Margen alrededor de la fila.

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
            shipRowMap.put(size, shipRow);  // Para acceder a cada HBox de barcos.
            shipSelectorContainer.getChildren().add(shipRow);
        }

        selectedShipCanvas = null;
        selectedShipSize = 0;
    }




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


    private void selectShipCanvas(Canvas oldCanvas, int size) {
        if (selectedShipCanvas != null) {
            selectedShipCanvas.setOpacity(1.0); // Deselecciona el anterior
        }
        boolean horizontal = !orientationToggle.isSelected();
        double canvasWidth = horizontal ? size * 30 : 30;
        double canvasHeight = horizontal ? 30 : size * 30;

        // Crea un nuevo Canvas
        Canvas newCanvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = newCanvas.getGraphicsContext2D();
        drawShipOnCanvas(gc, horizontal, size);
        newCanvas.setOpacity(0.6); // visualmente seleccionado

        // Reemplaza en el HBox el viejo canvas por el nuevo
        if (oldCanvas.getParent() instanceof Pane parent) {
            int index = parent.getChildrenUnmodifiable().indexOf(oldCanvas);
            if (index != -1) {
                parent.getChildren().set(index, newCanvas);
            }
        }

        selectedShipCanvas = newCanvas;
        selectedShipSize = size;
    }


    private void placePlayerShip(int row, int col) {
        if (selectedShipCanvas == null) {
            System.out.println("No hay barco seleccionado para colocar.");
            return;
        }

        int size = selectedShipSize;

        // Verificar si ya se alcanzó el límite de barcos para ese tamaño
        int placed = placedShipsCount.getOrDefault(size, 0);
        int maxAllowed = fleetComposition.getOrDefault(size, 0);
        if (placed >= maxAllowed) {
            System.out.println("Ya colocaste todos los barcos de tamaño " + size);
            return;
        }

        boolean horizontal = !orientationToggle.isSelected();

        Ship ship = playerBoardModel.placeShip(row, col, size, horizontal, true);
        if (ship != null) {
            playerShips.add(ship);

            drawShip(playerBoard, ship, true);

            // Actualizar contador
            placedShipsCount.put(size, placed + 1);

            // Eliminar el HBox correspondiente (igual que la figura sola).
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

            saveGameState();

            // Si no quedan más barcos por colocar, desactivar controles
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

                difficultySelector.getItems().addAll("Fácil", "Normal");
                difficultySelector.setOnAction(_ -> {
                    String difficulty = difficultySelector.getValue();
                    difficultySelector.setDisable(true);
                    selectedDifficulty = difficulty;
                    enemy.setDificulty(selectedDifficulty);
                    System.out.println("Dificultad seleccionada: " + selectedDifficulty);
                    difficultyLabel.setVisible(false);
                    difficultyLabel.setManaged(false);

                });
            }
        } else {
            System.out.println("No se pudo colocar el barco en esa posición.");
        }
    }

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


    //metodo que devuelve la celda (StackPane) de un GridPane y coordenas dado.
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

    //metodo que se encarga de manejar los disparos del jugador en la cuadricula de la maquina (modificado para que ahora no pase el turno si acierta o hunde un barco enemigo (en viceversa para la maquina)
    private void handlePlayerShot(int row, int col) throws DoubleShootException {
        if (!playerTurn || gameEnded) return; //si el turno es de la maquina o el juego ya acabo no hace nada
        boolean hit = player.makeMove(row, col, enemyBoardModel, enemyShips);

        StackPane cell = getStackPaneAt(enemyBoard, row, col);
        Canvas canvas = new Canvas(30, 30);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setMouseTransparent(true);
        canvas.setManaged(false);
        canvas.setUserData(hit ? "impacto" : "fallo");
        drawShot(gc, hit, false);
        cell.getChildren().add(canvas);

        //debugBoards();

        if(hit) {
            Ship ship = getShipAt(enemyShips, row, col);
            if(ship != null && ship.isSunk()) {
                System.out.println("Hundiste un barco.");
                drawSunkShips(ship, enemyBoard);
                //debugBoards();
            }

            checkWinCondition();
        } else {
            endPlayerTurn();
        }
        debugBoards();
        saveGameState();
        planeTextFileHandler.write("PlayerData.csv",player.getPlayerName() + "," + player.getPlayerScore());
    }

    //aqui se llama a la IA para que dispare, luego lo modificamos para que el jugador elija la dificultad
    private void triggerComputerMove() {
        playerTurn = false;
        boolean hit;

        if (enemy.getDificulty() == 1) {
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

        //debugBoards();

        if(hit) {
            Ship ship = getShipAt(playerShips, lastRow, lastCol);
            if(ship != null && ship.isSunk()) {
                drawSunkShips(ship, playerBoard);
                //debugBoards();
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

    private void endComputerTurn() {
        playerTurn = true;
        checkWinCondition();
    }

    private void endPlayerTurn(){
        playerTurn = false;
        checkWinCondition();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> triggerComputerMove());
            }}, 1000);
    }

    //metodo que devuelve el barco que esta en esa lista de barcos "ships" si no esta no devuelve nada
    public Ship getShipAt(List<Ship> ships, int row, int col) {
        for (Ship ship : ships) {
            if (ship.occupies(row, col)) {
                return ship;
            }
        }
        return null;
    }

    //Metodo que verifica si alguno de los dos jugadores (player o maquina) ah ganado (hundido todos los barcos de su rival)
    public void checkWinCondition() {
        if (gameEnded) return;

        boolean allEnemySunk = enemyShips.stream().allMatch(Ship::isSunk);
        boolean allPlayerSunk = playerShips.stream().allMatch(Ship::isSunk);

        if (allEnemySunk) {
            gameEnded = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "¡Ganaste! Has hundido todos los barcos enemigos.");
            alert.showAndWait();
            playerTurn = false;
            GameStage.deleteInstance();
            System.out.println("Eliminando la partida...");
            File file = new File("GameState.ser");
            if(!file.delete()) {
                System.out.println("Advertencia: No se pudo eliminar GameState.ser después de ganar la partida.");
            }
            continueGame = false;

        } else if (allPlayerSunk) {
            gameEnded = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "¡Has perdido! La máquina ha hundido todos tus barcos.");
            alert.showAndWait();
            playerTurn = false;
            GameStage.deleteInstance();
            System.out.println("Eliminando la partida...");
            File file = new File("GameState.ser");
            if(!file.delete()) {
                System.out.println("Advertencia: No se pudo eliminar GameState.ser después de perder.");
            }
            continueGame = false;
        }
    }

    //Este metodo maneja el evento generado por darle al boton de "listo" que indica haber terminado de poner los barcos
    @FXML
    private void handleFinishPlacement() {
        //si ya colocaste todos tus barcos empieza la fase de batalla; indica que finishedplacing = true y llama a placeEnemyShips() para que la maquina coloque sus barcos
        if (shipSelectorContainer.getChildren().isEmpty()) {
            musicPlayer.stop();
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

            //si aun no se colocan todos los barcos muestra una advertancia
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Coloca todos los barcos antes de continuar.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void placeEnemyShips() {
        Random rand = new Random();
        List<Integer> shipSizes = Arrays.asList(1, 2, 3, 4);

        // Ajusta la cantidad de barcos según la flota real (4 fragatas, 3 destructores, etc.)
        Map<Integer, Integer> fleetCount = Map.of(
                1, 4,  // 4 fragatas de tamaño 1
                2, 3,  // 3 destructores de tamaño 2
                3, 2,  // 2 submarinos de tamaño 3
                4, 1   // 1 portaaviones de tamaño 4
        );

        for (int size : shipSizes) {
            int count = fleetCount.getOrDefault(size, 1);
            for (int i = 0; i < count; i++) {
                boolean placed = false;
                while (!placed) {
                    int row = rand.nextInt(10);
                    int col = rand.nextInt(10);
                    boolean horizontal = rand.nextBoolean();
                    Ship ship = enemyBoardModel.placeShip(row, col, size, horizontal, false);
                    if (ship != null) {
                        enemyShips.add(ship);
                        drawShip(enemyBoard, ship, false);
                        placed = true;
                    }
                }
            }
        }
        debugBoards();
    }


    //Muestra o desactiva la vista de los barcos enemigos en la tabla de la maquina
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
        } else {
            monitorButton.setText("Mostrar embarcaciones enemigas");
        }
    }

    //metodo que dibuja los barcos dependiendo de su orientacion y tamaño (no usa canvas)
    private void drawBoatShape(GraphicsContext gc, boolean horizontal, boolean isFirst, boolean isLast, int shipLength, Image boatImage) {
        gc.clearRect(0, 0, 30, 30); //borra cualquier contenido previo en ese rectangulo de 30px  x 30 px

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
        double segmentWidth = boatWidth / 3; // Dividimos la imagen en 3 partes iguales

        // Sólo aplicar margen visual si es el portaaviones el que se está dibujando (longitud 4).
        double inset = (shipLength == 4) ? 0.2 : 0.0;   //El portaaviones sale como recortado, entonces hay que hacer que se vea junto.
        double destSize = 30 - 2 * inset;

        // Si es horizontal, no hacemos rotación
        if (horizontal) {
            if (isFirst) {
                // Dibuja la proa (primer segmento de la imagen)
                gc.drawImage(boatImage, 0, 0, segmentWidth, boatHeight, inset, inset, destSize, destSize); // Dibuja solo la proa
            } else if (isLast) {
                // Dibuja la popa (último segmento de la imagen)
                gc.drawImage(boatImage, 2 * segmentWidth, 0, segmentWidth, boatHeight, inset, inset, destSize, destSize); // Dibuja solo la popa
            } else {
                // Dibuja el medio (segmento central de la imagen)
                gc.drawImage(boatImage, segmentWidth, 0, segmentWidth, boatHeight, inset, inset, destSize, destSize); // Dibuja el medio
            }
        } else {
            // Si es vertical, necesitamos rotar la imagen para ajustarla
            gc.save();  // Guardamos el contexto actual del GraphicsContext
            gc.rotate(90); // Rotamos la imagen 90 grados

            if (isFirst) {
                // Dibuja la proa (primer segmento de la imagen) rotada
                gc.drawImage(boatImage, 0, 0, segmentWidth, boatHeight, inset, inset - 30, destSize, destSize); // Dibuja solo la proa (rotada)
            } else if (isLast) {
                // Dibuja la popa (último segmento de la imagen) rotada
                gc.drawImage(boatImage, 2 * segmentWidth, 0, segmentWidth, boatHeight, inset, inset - 30, destSize, destSize); // Dibuja solo la popa (rotada)
            } else {
                // Dibuja el medio (segmento central de la imagen) rotado
                gc.drawImage(boatImage, segmentWidth, 0, segmentWidth, boatHeight, inset, inset - 30, destSize, destSize); // Dibuja el medio (rotado)
            }

            gc.restore(); // Restauramos el contexto al estado original (sin rotación)
        }
    }

    public void debugBoards() {
        System.out.println("=== DEBUG: playerBoardModel     |        enemyBoardModel  ===");

        ArrayList<ArrayList<Boolean>> enemyGrid = enemyBoardModel.getEnemyBoard();
        ArrayList<ArrayList<Boolean>> playerGrid = playerBoardModel.getPlayerBoard();
        ArrayList<ArrayList<Boolean>> enemyShots = enemyBoardModel.getShotsOnEnemyBoard();
        ArrayList<ArrayList<Boolean>> playerShots = playerBoardModel.getShotsOnPlayerBoard();
        ArrayList<ArrayList<Boolean>> sunkEnemy = enemyBoardModel.getSunkEnemyShips();
        ArrayList<ArrayList<Boolean>> sunkPlayer = playerBoardModel.getSunkPlayerShips();

        int size = enemyGrid.size(); // Se asume que ambos tableros son del mismo tamaño

        for (int row = 0; row < size; row++) {
            String playerRow = buildDebugRow(playerGrid, playerShots, sunkPlayer, row);
            String enemyRow = buildDebugRow(enemyGrid, enemyShots, sunkEnemy, row);

            // Imprime la fila del tablero enemigo y del jugador lado a lado
            System.out.println(playerRow + "     " + enemyRow);
        }
    }

    // Permitirá la creación de las impresiones correctas de ambos tableros, para reducir código duplicado.
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


    //metodos para la serializcion: (¡Slava Rusia, Z!)

    //este metodo guarda el estado del juego adentro del archivo GameState.ser, debe ser llamado cada vez que el tablero se actualize
    public void saveGameState() {
        GameState state = new GameState(playerBoardModel, enemyBoardModel, playerShips, enemyShips, enemy);
        SerializableFileHandler handler = new SerializableFileHandler();
        handler.serialize("GameState.ser", state);
        System.out.println("Estado del juego guardado con exito en: GameState.ser");
    }

    //este metodo carga el juego desde el estado en el que se dejo cuando el jugador lo cerro
    public void loadGameState() {
        SerializableFileHandler handler = new SerializableFileHandler();
        GameState state = (GameState) handler.deserialize("GameState.ser");
        if (state != null) {
            this.playerBoardModel = state.getPlayerBoard();
            this.enemyBoardModel = state.getEnemyBoard();
            this.playerShips = state.getPlayerShips();
            this.enemyShips = state.getEnemyShips();
            this.enemy = state.getEnemy();
            redrawBoards();// redibuja los tableros aquí (gridPanes)
        }
    }

    //Este metodo re dibuja los disparos sobre los gridPane
    private void restoreShots(Board boardModel, GridPane board, boolean isPlayerBoard) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (!boardModel.alreadyShotAt(row, col, isPlayerBoard)) continue; //si no se ha disparado sobre esta celda pasa a la siguiente iteracion (continue)

                StackPane cell = getStackPaneAt(board, row, col);
                if (cell == null) continue; //si la celda esta nula continua con la siguiente iteracion (continue)

                boolean skip = false; //esta nos ayuda a saber si ya esta dibujad sobre aquel celda
                for (Node node : cell.getChildren()) { //recorre cada node de la celda
                    if ("hundido".equals(node.getUserData())) { //si en uno de los nodos hay un hundido entonces pone a skip = true y brekea el for
                        skip = true; //si haya un nodo "hundido" breakea el for de nodos y pone skip = true
                        break;
                    }
                }
                if (skip) continue; //si skip es igual a true, osea si hayo un nodo adentro de la celdas que se llame hundido entonces salta la iteracion actual y pasa a la siguente

                // Dibujar el disparo
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

    private void redrawBoards() {
        // Limpia los tableros visuales
        createBoard(playerBoard, true);
        createBoard(enemyBoard, false);

        // Dibuja los barcos del jugador
        for (Ship ship : playerShips) {
            drawShip(playerBoard, ship, true);
            if (ship.isSunk()) {
                drawSunkShips(ship, playerBoard);
            }
        }

        // Dibuja los barcos del enemigo (ocultos)
        for (Ship ship : enemyShips) {
            drawShip(enemyBoard, ship, false);
            if (ship.isSunk()) {
                drawSunkShips(ship, enemyBoard);
            }
        }

        // Restaura disparos del jugador sobre enemigo
        restoreShots(enemyBoardModel, enemyBoard, true);
        // Restaura disparos de la máquina sobre jugador
        restoreShots(playerBoardModel, playerBoard, false);
    }

    //este metodo dibuja la imagen en el canvas de la celda dependiendo de si esta hundido, golpeado o no le dio a nada
    public void drawShot(GraphicsContext gc, boolean isHit, boolean isSunk) {
        gc.clearRect(0, 0, 30, 30);
        Image imgToDraw = isSunk ? explosion : (isHit ? smoke : miss);
        gc.drawImage(imgToDraw, 0, 0, 30, 30);
        gc.restore();
    }

    //metodo para dibujar un barco como hundido
    public void drawSunkShips(Ship ship, GridPane board) {
        List<Ship.Coordinate> coords = ship.getCoordinates();
        for (Ship.Coordinate coordinate : coords) {
            int row = coordinate.getRow();
            int col = coordinate.getCol();
            StackPane cell = getStackPaneAt(board, row, col);
            if (cell != null) {
                cell.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().equals("impacto")); //elimina el node que antes representaba el impacto sobre el barco, esto para evitar la sobreposicion de imagenes :v
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