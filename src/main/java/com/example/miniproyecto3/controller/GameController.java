package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.Board;
import com.example.miniproyecto3.model.GameState;
import com.example.miniproyecto3.model.Ship;
import com.example.miniproyecto3.view.GameStage;
import com.example.miniproyecto3.view.WelcomeStage;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import com.example.miniproyecto3.model.serializable.SerializableFileHandler;
import com.example.miniproyecto3.model.planeTextFiles.PlaneTextFileHandler;
import com.example.miniproyecto3.model.Player;
import java.io.File;


import java.io.IOException;
import java.util.*;

public class GameController {

    //Objetos del Fxml (visual)
    @FXML
    private GridPane playerBoard;
    @FXML
    private GridPane enemyBoard;
    @FXML
    private ComboBox<Integer> shipSizeSelector;
    @FXML
    private ToggleButton orientationToggle;
    @FXML
    private Button monitorButton;
    @FXML
    private Button readyButton;
    @FXML
    private VBox vBoxCont;


    //Objetos para llevar la logica interna del juego
    private Board playerBoardModel = new Board();
    private Board enemyBoardModel = new Board();
    private List<Ship> playerShips = new ArrayList<>();
    private List<Ship> enemyShips = new ArrayList<>();
    private StackPane[][] enemyCells = new StackPane[10][10]; //no se está usando para nada :v (solo en createBoard pero sin ninguna funcionalidad real)

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

    @FXML
    public void initialize() throws IOException { //Esta funcion es el punto de partida de la ventana GameStage, cualquier Fmxl tiene una de estas y se llama automaticamente al abrir una instancia de GameStage
        planeTextFileHandler = new PlaneTextFileHandler();
        continueGame = WelcomeStage.getInstance().getWelController().getContinue();
        WelcomeStage.deleteInstance();
        vBoxCont.setSpacing(10);
        //File file = new File("GameState.ser");
        if(!continueGame) { //Si el jugador le dio a jugar (no continuar) el juego crea una nueva partida desde 0
            System.out.println("Nuevo juego...");
            System.out.println("Creando playerboard");
            createBoard(playerBoard, true);
            System.out.println("Creando enemyboard");
            createBoard(enemyBoard, false);
            System.out.println("seleccionando primera opcion en shipSizeSelector");
            shipSizeSelector.getSelectionModel().selectFirst();
            System.out.println("Creando evento para orientationToggle");
            orientationToggle.setOnAction(e -> toggleOrientation());
            System.out.println("Desactivando monitorMode");
            monitorButton.setDisable(true);
        }
        else if(continueGame) { //Si el jugador le dio a continuar carga la partida mas reciente :v
            System.out.println("Entrando a cargar el juego mas reciente");
            finishedPlacing = true;
            loadGameState();
            //checkWinCondition();
            readyButton.setDisable(true);
            orientationToggle.setDisable(true);
            shipSizeSelector.setDisable(true);
        }

    }

    //Este metodo crea los gridpanes (tableros visuales) de amboss jugadores, tanto jugador como maquina
    private void createBoard(GridPane board, boolean isPlayer) {
        System.out.println("Creando " + isPlayer + " board (gridpane)" );
        board.getChildren().clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                System.out.println("Creando  + isPlayer +  board (gridpane). Coords: )" + row + "," + col);
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
                        System.out.println("poniendo barco del jugador en la posicion " + finalRow + "," + finalCol);
                        placePlayerShip(finalRow, finalCol);
                    } else if (finishedPlacing && !isPlayer && e.getButton() == MouseButton.PRIMARY) {
                        System.out.println("Disparando en la posicion " + finalRow + "," + finalCol);
                        handlePlayerShot(finalRow, finalCol);
                    }
                });

                board.add(cell, col, row);
            }
        }
        board.setHgap(0);
        board.setVgap(0);
    }

    private void toggleOrientation() {
        if (orientationToggle.isSelected()) {
            orientationToggle.setText("Vertical");
        } else {
            orientationToggle.setText("Horizontal");
        }
    }


    //metodo que pone los barcos del jugador en las coordenas (row,col)
    private void placePlayerShip(int row, int col) {
        Image boatImage = new Image(getClass().getResource("/prueba.png").toExternalForm());
        int size = shipSizeSelector.getValue();
        boolean horizontal = !orientationToggle.isSelected();
        Ship ship = playerBoardModel.placeShip(row, col, size, horizontal, true);

        if (ship != null) {
            //si el barco se creo con exito lo añade al arrayList de barcos del jugador, lo dibuja y elimina ese barco de la lista.
            playerShips.add(ship);
            placeShipVisual(playerBoard, ship, boatImage);
            shipSizeSelector.getItems().remove((Integer) size);
            saveGameState();
            //si ya se acabaron todas las opciones para poner barcos elimina el boton de seleccion de barcos y orientacion (los deshabilita) y activa el boton de modo monitor
            if (shipSizeSelector.getItems().isEmpty()) {
                shipSizeSelector.setDisable(true);
                orientationToggle.setDisable(true);
                monitorButton.setDisable(false);
            }
        }
    }

    //metodo dibuja visualmente en el gridPane ya se de jugdor o maquina el barco que se le pase  con su respectiva imagen
    private void placeShipVisual(GridPane board, Ship ship, Image boatImage) {
        List<int[]> coords = ship.getCoordinates();

        //recorre cada par de coordenas del barco (rox,col)
        for (int i = 0; i < coords.size(); i++) {
            int[] coord = coords.get(i); //saca cada par de coordenas y lo ingresa en un array
            int row = coord[0]; //define la fila actual de este pedazo de barco
            int col = coord[1]; //define la columna actual del pedazo de barco

            boolean isFirst = (i == 0); //determina si este par de coordenas es la proa o inicio del barco
            boolean isLast = (i == coords.size() - 1); //determina si este par de coordenas es el ultimo o la popa

            StackPane cell = getStackPaneAt(board, row, col); //se obtiene la celda grafica del gridpane en esa coordenada, esta celda es un stackPane
            //si existe la celda; crea un canvas (30px X 30px), obtienes el graphicContext del canvas para dibujar sobre el, llamamos al metodo para dibujar cada parte del barco y por ultimo añadimos el canvas a la celda (StackPane) del gridpane.
            if (cell != null) {
                Canvas canvas = new Canvas(30, 30);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                // Llamamos al método para dibujar la imagen del barco
                drawBoatShape(gc, ship.isHorizontal(), isFirst, isLast, boatImage);
                canvas.setMouseTransparent(true);
                cell.getChildren().add(canvas);
            }
        }
    }


    private void placeShipVisualHidden(GridPane board, Ship ship) {
        //recorre cada par de coordenas del barco y obtiene la celda stackPane de aquel par de coordenadas
        for (int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            StackPane cell = getStackPaneAt(board, row, col);
            //si obtuvo la celda; crea un rectangulo (30px X 30px), lo rellena de verde, lo vuelve o no visible dependiendo de si el usuario activa el monitorMode y evita que se interponga entre el mouse en el stage.
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
    //metodo que devuelve la celda (StackPane) de un GridPane y coordenas dado.
    private StackPane getStackPaneAt(GridPane board, int row, int col) {
        for (javafx.scene.Node node : board.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (StackPane) node;
            }
        }
        return null;
    }

    //metodo que se encarga de manejar los disparos del jugador en la cuadricula de la maquina
    private void handlePlayerShot(int row, int col) {
        if(!playerTurn || gameEnded) return; //si el turno es de la maquina o el juego ya acabo no hace nada

        StackPane cell = getStackPaneAt(enemyBoard, row, col); //obtiene la celda (StackPane) de la cuadricula del enemigo en las coords dadas
        //assert cell != null;
        if(cell == null) return;

        for(javafx.scene.Node child : cell.getChildren()) { //para cada node en la celda
            if(child instanceof Label label && (label.getText().equals("X") || label.getText().equals("O"))) { //si ya han disparado ahi no hace nada
                return;
            }
        }
        //Button btn = (Button) cell.getChildren().get(0);

        //if (!btn.getText().isEmpty()) return;

        Ship hitShip = enemyBoardModel.shoot(row, col, true); //Si fue un acierto retorna el barco afectado si no entocnes retorna null
        saveGameState();
        if (hitShip != null) {
            System.out.println("Shot at " + row + ", " + col);
            //btn.setDisable(true);
            //cell.getChildren().remove(btn);
            Label hitLabel = new Label("X");
            hitLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red; -fx-font-weight: bold;");
            cell.getChildren().add(hitLabel);
            saveGameState();

            if(hitShip.isSunk()) { //si fue hundido entonces llama highlightSunkShip para pintarlo como hundido y tambien actualiza el puntaje del jugador
                player.setPlayerScore(player.getPlayerScore() + 1);
                planeTextFileHandler.write("PlayerData.csv", player.getPlayerName() + "," + player.getPlayerScore());
                highlightSunkShip(hitShip);
                saveGameState();
            }
            //btn.setDisable(true);
            //btn.setText("X");
            //btn.setStyle("-fx-background-color: red;");
            handlePlayerShot(row, col);
        } else {
            //si no fue un acierto entonces pinta una O azul
            Label missLabel = new Label("O");
            missLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: blue; -fx-font-weight: bold;");
            cell.getChildren().add(missLabel);
            saveGameState();
            //btn.setText("O");
            //btn.setStyle("-fx-background-color: white;");
        }

        playerTurn = false; //pasa el turno a la maquina
        checkWinCondition(); //checkea victoria
        saveGameState();

        if(gameEnded) return; //si el juego termino breakea

        //espera 1 segundo y luego llama a handleComputerShot() para que dispare, le devuelve el turno a el jugador y checkea victoria
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    handleComputerShot();
                    playerTurn = true;
                    checkWinCondition();
                    saveGameState();
                });

            }
        }, 1000);
    }

    //metodo que dibuja el hundimiento de un barco enemigo
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

    //metood que dibuja el hundimiento de un barco del jugador
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

    //metodo que maneja los disparos de la maquina
    private void handleComputerShot() {
        //Elegi un numero random para la columna y fila en el que nunca se ah disparado
        Random rand = new Random();
        int row, col;

        do{
            row = rand.nextInt(10);
            col = rand.nextInt(10);
        } while (playerBoardModel.alreadyShotAt(row, col, false));



        playerBoardModel.registerShot(row, col, false); //registra el tiro en la tabla del jugador

        StackPane cell = getStackPaneAt(playerBoard, row, col); //obtiene la celda del jugador donde se disparo
        if(cell == null) return;

        if(playerBoardModel.hasShipAt(row, col, true)) { //si la celda tiene un barco entonces devuelve el barco, si no devuelve null
            Ship hitShipAtPosition = getShipAt(playerShips, row, col); //devuelve el barco al que se le disparo
            if(hitShipAtPosition != null) {
                System.out.println("Shot at (by the machine): " + row + ", " + col);
                hitShipAtPosition.registerHit(row, col); //registra el acierto en el barco
                Label hitMachineLabel = new Label("X");
                hitMachineLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red; -fx-font-weight: bold;");
                cell.getChildren().add(hitMachineLabel);
                //si lo hunde entonces lo pinta como hundido en la tabla del jugadr
                if(hitShipAtPosition.isSunk()) {
                    highlightPlayerSunkShip(hitShipAtPosition);
                }
            }
        //si no le acerto a ningun barco entonces lo pinta como falla en el tablero del jugador
        } else {
            Label missMachineLabel = new Label("O");
            missMachineLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: blue; -fx-font-weight: bold;");
            cell.getChildren().add(missMachineLabel);
        }
    }

    //metodo que devuelve el barco que esta en esa lista de barcos "ships" si no esta no devuelve nada
    private Ship getShipAt(List<Ship> ships, int row, int col) {
        for(Ship ship : ships) {
            if(ship.occupies(row, col)) {
                return ship;
            }
        }
        return null;
    }

    //Metodo que verifica si alguno de los dos jugadores (player o maquina) ah ganado (hundido todos los barcos de su rival)
    private void checkWinCondition() {
        if(gameEnded) return;

        boolean allEnemySunk = enemyShips.stream().allMatch(Ship::isSunk);
        boolean allPlayerSunk = playerShips.stream().allMatch(Ship::isSunk);

        if(allEnemySunk) {
            gameEnded = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "¡Ganaste! Has hundido todos los barcos enemigos.");
            alert.showAndWait();
            playerTurn = false;
            if(continueGame)
            {
                System.out.println("Eliminando la partida...");
                File file = new File("GameState.ser");
                file.delete();
            }
            GameStage.deleteInstance();

        } else if(allPlayerSunk) {
            gameEnded = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "¡Has perdido! La máquina ha hundido todos tus barcos.");
            alert.showAndWait();
            playerTurn = false;
            if(continueGame)
            {
                System.out.println("Eliminando la partida...");
                File file = new File("GameState.ser");
                file.delete();
            }
            GameStage.deleteInstance();
        }
        //saveGameState();
    }

    //Este metodo maneja el evento generado por darle al boton de "listo" que indica haber terminado de poner los barcos
    @FXML
    private void handleFinishPlacement() {
        //si ya colocaste todos tus barcos empieza la fase de batalla; indica que finishedplacing = true y llama a placeEnemyShips() para que la maquina coloque sus barcos
        if (shipSizeSelector.getItems().isEmpty()) {
            finishedPlacing = true;
            placeEnemyShips();
            readyButton.setDisable(finishedPlacing);
            saveGameState();
            //si aun no se colocan todos los barcos muestra una advertancia
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Coloca todos los barcos antes de continuar.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Pone los barcos enemigos de modo aleatorio; los ingresa en la enemyBoardModel y en la enemyShips.
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
                    //saveGameState();
                }
            }

        }
        debugEnemyBoard();
    }

    //Muestra o desactiva la vista de los barcos enemigos en la tabla de la maquina
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

    //metodo que dibuja los barcos dependiendo de su orientacion y tamaño (no usa canvas)
    private void drawBoatShape(GraphicsContext gc, boolean horizontal, boolean isFirst, boolean isLast, Image boatImage) {
        gc.clearRect(0, 0, 30, 30);

        // Efecto de sombra
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        shadow.setColor(Color.rgb(30, 30, 30, 0.4));
        gc.applyEffect(shadow);

        // Asumiendo que la imagen del barco está dividida en tres partes:
        // 1. Proa (primer segmento)
        // 2. Medio (varios segmentos)
        // 3. Popa (último segmento)

        double boatWidth = boatImage.getWidth();
        double boatHeight = boatImage.getHeight();
        double segmentWidth = boatWidth / 3; // Dividimos la imagen en 3 partes iguales

        // Si es horizontal, no hacemos rotación
        if (horizontal) {
            if (isFirst) {
                // Dibuja la proa (primer segmento de la imagen)
                gc.drawImage(boatImage, 0, 0, segmentWidth, boatHeight, 0, 0, 30, 30); // Dibuja solo la proa
            } else if (isLast) {
                // Dibuja la popa (último segmento de la imagen)
                gc.drawImage(boatImage, 2 * segmentWidth, 0, segmentWidth, boatHeight, 0, 0, 30, 30); // Dibuja solo la popa
            } else {
                // Dibuja el medio (segmento central de la imagen)
                gc.drawImage(boatImage, segmentWidth, 0, segmentWidth, boatHeight, 0, 0, 30, 30); // Dibuja el medio
            }
        } else {
            // Si es vertical, necesitamos rotar la imagen para ajustarla
            gc.save();  // Guardamos el contexto actual del GraphicsContext
            gc.rotate(90); // Rotamos la imagen 90 grados

            if (isFirst) {
                // Dibuja la proa (primer segmento de la imagen) rotada
                gc.drawImage(boatImage, 0, 0, segmentWidth, boatHeight, 0, -30, 30, 30); // Dibuja solo la proa (rotada)
            } else if (isLast) {
                // Dibuja la popa (último segmento de la imagen) rotada
                gc.drawImage(boatImage, 2 * segmentWidth, 0, segmentWidth, boatHeight, 0, -30, 30, 30); // Dibuja solo la popa (rotada)
            } else {
                // Dibuja el medio (segmento central de la imagen) rotado
                gc.drawImage(boatImage, segmentWidth, 0, segmentWidth, boatHeight, 0, -30, 30, 30); // Dibuja el medio (rotado)
            }

            gc.restore(); // Restauramos el contexto al estado original (sin rotación)
        }
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


    //metodos para la serializcion: (¡Slava Rusia, Z!)

    //este metodo guarda el estado del juego adentro del archivo GameState.ser, debe ser llamado cada vez que el tablero se actualize
    public void saveGameState(){
        GameState state = new GameState(playerBoardModel, enemyBoardModel, playerShips, enemyShips);
        SerializableFileHandler handler = new SerializableFileHandler();
        handler.serialize("GameState.ser", state);
        System.out.println("Estado del juego guardado con exito en: GameState.ser");
    }

    //este metodo carga el juego desde el estado en el que se dejo cuando el jugador lo cerro
    public void loadGameState( ){
        SerializableFileHandler handler = new SerializableFileHandler();
        GameState state = (GameState) handler.deserialize("GameState.ser");

        if (state != null) {
            this.playerBoardModel = state.getPlayerBoard();
            this.enemyBoardModel = state.getEnemyBoard();
            this.playerShips = state.getPlayerShips();
            this.enemyShips = state.getEnemyShips();
            redrawBoards();// redibujar los tableros aquí (gridPanes)
        }
    }

    public void continueB(boolean isContinue) {
        if(isContinue) {
            //planeTextFileHandler = new PlaneTextFileHandler();
            String data[] = planeTextFileHandler.read("PlayerData.csv");
            String user = data[0];
            int score = Integer.parseInt(data[1]);
            player = new Player(user, score);
            System.out.println("Jugador: " + player.getPlayerName() + "," + player.getPlayerScore());

            //loadGameState();
        }
        else{
            String data[] = planeTextFileHandler.read("PlayerData.csv");
            String user = data[0].trim();
            player = new Player(user, 0);
            System.out.println("JugadorNuevo: " + player.getPlayerName() + "," + player.getPlayerScore());
        }
    }

    public void continueB(boolean isContinue, boolean equals){
        String data[] = planeTextFileHandler.read("PlayerData.csv");
        String user = data[0].trim();
        int score = Integer.parseInt(data[1]);
        player = new Player(user, score);
        System.out.println("JugadorCasiNuevo: " + player.getPlayerName() + "," + player.getPlayerScore());
    }

    //este metodo redibujara los tableros, tanto para el jugador como la maquina (cuando el jugador le da continuar)
    private void redrawBoards() {
        Image boatImage = new Image(getClass().getResource("/prueba.png").toExternalForm());

        // Limpia los tableros visuales
        createBoard(playerBoard, true);
        createBoard(enemyBoard, false);

        // Dibuja los barcos del jugador
        for (Ship ship : playerShips) {
            placeShipVisual(playerBoard, ship, boatImage);
            if(ship.isSunk()){
                highlightSunkShip(ship);
            }
        }

        // Dibuja los barcos del enemigo (ocultos)
        for (Ship ship : enemyShips) {
            placeShipVisualHidden(enemyBoard, ship);
            if(ship.isSunk()){
                highlightSunkShip(ship);
            }
        }

        // Restaura disparos del jugador sobre enemigo
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (enemyBoardModel.alreadyShotAt(row, col, true)) {
                    StackPane cell = getStackPaneAt(enemyBoard, row, col);
                    if (cell != null) {
                        if (enemyBoardModel.hasShipAt(row, col, false)) {

                            Label hitLabel = new Label("X");
                            hitLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red; -fx-font-weight: bold;");
                            cell.getChildren().add(hitLabel);
                        } else {
                            Label missLabel = new Label("O");
                            missLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: blue; -fx-font-weight: bold;");
                            cell.getChildren().add(missLabel);
                        }
                    }
                }
            }
        }

        // Restaura disparos de la máquina sobre jugador
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (playerBoardModel.alreadyShotAt(row, col, false)) {
                    StackPane cell = getStackPaneAt(playerBoard, row, col);
                    if (cell != null) {
                        if (playerBoardModel.hasShipAt(row, col, true)) {
                            Label hitLabel = new Label("X");
                            hitLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red; -fx-font-weight: bold;");
                            cell.getChildren().add(hitLabel);
                        } else {
                            Label missLabel = new Label("O");
                            missLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: blue; -fx-font-weight: bold;");
                            cell.getChildren().add(missLabel);
                        }
                    }
                }
            }
        }
    }
}

