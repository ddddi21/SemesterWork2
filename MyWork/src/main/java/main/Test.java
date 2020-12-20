package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import drawing.Draw;
import network.Connection;
import network.ConnectionListener;
import services.SendDrawingService;

import java.io.IOException;
import java.util.ArrayList;


public class Test extends Application implements ConnectionListener {
    private final ArrayList<Connection> connectionArrayList = new ArrayList<>();
    Draw draw = new Draw();
    /*SendDrawingService drawingService = new SendDrawingService();*/  //Нам ето не нужон
    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            connection = new Connection(this, "localhost", 8181);
            System.out.println("Test connection: "+ connection);
            System.out.println("Test socket: "+ connection.socket);
//            txtAreaDisplay.appendText("Connected. \n");
        } catch (IOException e) {
//            printMessage("Connection exception: " + e);
        }
        draw.connection = this.connection;
        draw.id = connection.id;

        //отрисовываем первое окошко входа в игру
        Group start = new Group();
        Button startGame = new Button("start game");


        start.getChildren().addAll(startGame);

        //новое окошко при нажатии "начать игру"
        Scene scene = new Scene(start, 800, 625);
        startGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage stage = new Stage();
                    draw.start(stage);
                    if (connection.isCommander)
                        printMessage("you are drawing!");
                    else printMessage("ты угадываешь!");
                    ((Node) (event.getSource())).getScene().getWindow().hide();
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
    }

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
        System.out.println("Test/Recive: "+value);
        if (!value.equals("null")) {
            if (value.equals("StartFirst")) {
                connection.isCommander = true;
                System.out.println("Найден командер");
            } else if (value.equals("GameIsStarting")) {
                onStartDrawing(connection, true);
                System.out.println("Начало игры");
            } else if((value.charAt(0)+"").equals("#")){
                if((value.charAt(1)+"").equals("s")){
                    String info = value.substring(2);
                    System.out.println("Test/Получил: "+info);
                    if(!connection.isCommander) draw.startDraw(draw.graphicsContext,info);
                }else if ((value.charAt(1)+"").equals("m")) {
                    String info = value.substring(2);
                    System.out.println("Test/Получил: "+info);
                    if(!connection.isCommander) draw.draw(draw.graphicsContext,info);
                }
                else if((value.charAt(1)+"").equals("c")){
                    if(!connection.isCommander) draw.clear();
                }
                else printMessage(value);
            }else printMessage(value);
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

    private synchronized void printMessage(String message) {
        Platform.runLater(() -> {
            Draw.txtAreaDisplay.appendText(message + "\n");
        });
    }

}

