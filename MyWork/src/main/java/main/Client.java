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


public class Client extends Application implements ConnectionListener {
    public Connection connection;
    public Draw draw = new Draw();
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
    }

    public static void main(String[] args) {
        launch(args);
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
        if(!value.equals("null")) {
            if (value.equals("StartFirst")) {
                connection.isCommander = true;
            } else {
                //принимаю что началась рисовка
                if (value.equals("GameIsStarting")) {
                    onStartDrawing(connection, true);
                } else{
                    //проверяю на угаданное слово
                    if(value.equals("win")){
                        printMessage("Вы победили!");
                        theEnd();
                    }
                        else {
                        connection.guessWord = draw.guessWord;
                            printMessage(value);
                    }
                }
            }
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

    //возможно в будущем добавлю вывод имени угадавшего
    private void theEnd(){

    }

}

