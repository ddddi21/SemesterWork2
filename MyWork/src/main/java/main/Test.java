package main;

import drawing.Draw;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.Connection;
import network.ConnectionListener;
import services.SendDrawingService;

import java.io.IOException;
import java.util.ArrayList;


public class Test extends Application implements ConnectionListener {
    ColorPicker colorPicker;
    public javafx.scene.control.TextField txtName;
    public javafx.scene.control.TextField txtInput;
    javafx.scene.control.ScrollPane scrollPane;
    public javafx.scene.control.TextField roomField;
    public TextArea txtAreaDisplay;
    private Connection connection;
    Draw draw = new Draw();
    SendDrawingService drawingService = new SendDrawingService();
    private final ArrayList<Connection> connectionArrayList = new ArrayList<>();



    @Override
    public void start(Stage primaryStage) {
        try {
            connection = new Connection(this, "localhost", 8181);
//            txtAreaDisplay.appendText("Connected. \n");
        } catch (IOException e) {
//            printMessage("Connection exception: " + e);
        }
        draw.connection = this.connection;
        draw.id = connection.id;

        //отрисовываем первое окошко входа в игру
        Group start = new Group();
        Button startGame = new Button("start game");
//        scrollPane = new javafx.scene.control.ScrollPane();
//        txtAreaDisplay = new TextArea();
//        txtAreaDisplay.setEditable(false);
//        scrollPane.setContent(txtAreaDisplay);
//        scrollPane.setFitToHeight(true);
//        scrollPane.setFitToWidth(true);

        start.getChildren().addAll(startGame);

        //новое окошко при нажатии "начать игру"
        Scene scene = new Scene(start, 800, 625);
        startGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage stage = new Stage();
                    draw.start(stage);
                    if(connection.isCommander)
                    printMessage("you are drawing!");
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        primaryStage.setTitle("крокодильчик");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                onDisconnect(connection);
                connection.disconnect();
            }
        });

//        Group root = new Group();
//        VBox vBox = new VBox();
//        scrollPane = new javafx.scene.control.ScrollPane();
//        HBox hBox = new HBox();
//
//        txtAreaDisplay = new TextArea();
//        txtAreaDisplay.setEditable(false);
//        scrollPane.setContent(txtAreaDisplay);
//        scrollPane.setFitToHeight(true);
//        scrollPane.setFitToWidth(true);
//
//
//        final Canvas canvas = new Canvas(600, 300);
//        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
//        initDraw(graphicsContext);
//
//        if(connection.isCommander) {
//            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
//                    new EventHandler<MouseEvent>() {
//
//                        @Override
//                        public void handle(MouseEvent event) {
//                            graphicsContext.beginPath();
//                            graphicsContext.moveTo(event.getX(), event.getY());
//                            graphicsContext.setStroke(colorPicker.getValue());
//                            graphicsContext.stroke();
//                        }
//                    });
//
//            canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
//                    new EventHandler<MouseEvent>() {
//
//                        @Override
//                        public void handle(MouseEvent event) {
//                            graphicsContext.lineTo(event.getX(), event.getY());
//                            graphicsContext.setStroke(colorPicker.getValue());
//                            graphicsContext.stroke();
//                        }
//                    });
//
//            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
//                    new EventHandler<MouseEvent>() {
//
//                        @Override
//                        public void handle(MouseEvent event) {
//
//                        }
//                    });
//
//            Button clean = new Button();
//
//            clean.setText("clean all");
//            clean.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    double canvasWidth = graphicsContext.getCanvas().getWidth();
//                    double canvasHeight = graphicsContext.getCanvas().getHeight();
//
//                    graphicsContext .clearRect(0,0,canvasWidth,canvasHeight);
//                    graphicsContext.setStroke(Color.BLACK);
//                    graphicsContext.setLineWidth(5);
//                    graphicsContext.fill();
//                    graphicsContext.strokeRect(
//                            0,              //x of the upper left corner
//                            0,              //y of the upper left corner
//                            canvasWidth,    //width of the rectangle
//                            canvasHeight);  //height of the rectangle
//                    graphicsContext.setStroke(colorPicker.getValue());
//                    graphicsContext.setLineWidth(3);
//                }
//            });
//
//            HBox topBox = new HBox();
//
//            topBox.getChildren().addAll(colorPicker,clean);
//            HBox.setHgrow(clean,Priority.ALWAYS);
//            HBox.setHgrow(colorPicker,Priority.ALWAYS);
//            vBox.getChildren().addAll(topBox,canvas, scrollPane);
//
//        } else{
//            txtName = new javafx.scene.control.TextField();
//            txtName.setPromptText("Name");
//            txtName.setTooltip(new Tooltip("Write your name. "));
//            txtInput = new javafx.scene.control.TextField();
//            txtInput.setPromptText("New message");
//            txtInput.setTooltip(new Tooltip("Write your message. "));
//            javafx.scene.control.Button btnSend = new Button("Send");
//            btnSend.setOnAction(new ButtonListener());
//            hBox.getChildren().addAll(txtName, txtInput, btnSend);
//            HBox.setHgrow(txtInput, Priority.ALWAYS);
//            vBox.getChildren().addAll(canvas, scrollPane, hBox);
//        }
//
//        vBox.setVgrow(scrollPane, Priority.ALWAYS);
//        root.getChildren().add(vBox);
//        Scene scene = new Scene(root, 800, 625);
//        primaryStage.setTitle("крокодильчик");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        txtAreaDisplay.appendText("Connected. \n");
    }

    public static void main(String[] args) {
        launch(args);
    }


//    private void initDraw(GraphicsContext gc) {
//
//        colorPicker = new ColorPicker();
//
//        double canvasWidth = gc.getCanvas().getWidth();
//        double canvasHeight = gc.getCanvas().getHeight();
//
//        gc.setFill(Color.LIGHTGRAY);
//        gc.setStroke(Color.BLACK);
//        gc.setLineWidth(5);
//
//        gc.fill();
//        gc.strokeRect(
//                0,              //x of the upper left corner
//                0,              //y of the upper left corner
//                canvasWidth,    //width of the rectangle
//                canvasHeight);  //height of the rectangle
//
//        gc.setFill(colorPicker.getValue());
//        gc.setStroke(colorPicker.getValue());
//        gc.setLineWidth(3);
//    }
//    public void fillOvalX(double x, double y, double w, double h) {
//        // a possible approach
//        commands.put(new DrawCommand(Type.FILL_OVAL, x, y, w, h));
//        g.fillOval(x, y, w, h);
//    }
@Override
public void onConnectionReady(Connection connection) {
//    connectionArrayList.add(connection);
//    while(true){
//        if (connectionArrayList.get(0) == connection) {
//           connection.isCommander = true;
//            break;
//        }
//    }
//    printMessage("Connection ready..");
}

    @Override
    public void onReceiveString(Connection connection, String value) {
        if(!value.equals("null")) {
            if (value.equals("StartFirst")) {
                connection.isCommander = true;
            }
            //принимаю что началась рисовка
            if(value.equals("GameIsStarting")){
                onStartDrawing(connection, true);
            }
            else printMessage(value);
        }
    }

    @Override
    public void onDisconnect(Connection connection) {
//        printMessage("Connection close: " + connection);

    }

    @Override
    public void onException(Connection connection, Exception e) {
//        printMessage("Connection exception: " + e);
    }

    //присваиваю тру в классе где я потом этот тру должна видеть чтобы начать отрисоввыать но все пошло по одному месту
    @Override
    public void onStartDrawing(Connection connection, boolean isStart) {
        draw.isStart = true;
    }

    private synchronized void printMessage(String message){
        Platform.runLater(() -> {
            Draw.txtAreaDisplay.appendText(message + "\n");
        });
    }

//    private class ButtonListener implements EventHandler<javafx.event.ActionEvent> {
//        Connection connection;
//
//        @Override
//        public void handle(javafx.event.ActionEvent e) {
//            String username = txtName.getText().trim();
//            String message = txtInput.getText().trim();
//
//            if (username.length() == 0) {
//                username = "Unknown";
//            }
//            if (message.length() == 0) {
//                return;
//            }
//
//            connection.sendString("[" + username + "]: " + message + "");
//
//            txtInput.clear();
//
//        }
//    }
}

