package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import drawing.Draw;
import network.Connection;
import network.ConnectionListener;

import java.io.IOException;


public class Client extends Application implements ConnectionListener {
    Draw draw = new Draw();
    private Connection connection;

    @FXML
    Group start;
    @FXML
    Button startGame;
    @FXML
    TextArea textArea;
    @FXML
    VBox vBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lobby.fxml"));
            connection = new Connection(this, "25.32.229.34", 54181);
            System.out.println("Test connection: "+ connection);
            System.out.println("Test socket: "+ connection.socket);
//            txtAreaDisplay.appendText("Connected. \n");
        } catch (IOException e) {
//            printMessage("Connection exception: " + e);
        }
        draw.connection = this.connection;
        draw.id = connection.id;

        //отрисовываем первое окошко входа в игру
        start = new Group();
        startGame = new Button("Start game");
        startGame.setPrefWidth(525);
        startGame.setAlignment(Pos.CENTER);
        textArea = new TextArea("\n\n \n                                                           КРОКОДИЛ");
        textArea.setPrefHeight(200);
        textArea.setEditable(false);
        vBox = new VBox();
        vBox.getChildren().addAll(textArea,startGame);

        start.getChildren().addAll(vBox);

        //новое окошко при нажатии "начать игру"
        Scene scene = new Scene(start, 500, 425);
        startGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage stage = new Stage();
                    draw.start(stage);
                    if (connection.isCommander)
                        printMessage("you are drawing!");
                    else printMessage("you are guess!");
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (Exception e) {
                    onException(connection,e);
                }
            }
        });
        primaryStage.setTitle("crocodile");
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
//    printMessage("Connection ready..");
    }

    @Override
    public void onReceiveString(Connection connection, String value) {
        System.out.println("Test/Recive: "+value);
        if (!value.equals("null")) {
            if (value.equals("StartFirst")) {
                connection.isCommander = true;
                System.out.println("Найден командер");
            }
                else if((value.charAt(0)+"").equals("#")){
                    switch ((value.charAt(1) + "")) {
                        //если ведущий начал рисовать, начинаем отрисовывать другим игрокам
                        case "s": {
                            String info = value.substring(2);
                            System.out.println("Test/Получил: " + info);
                            if (!connection.isCommander) draw.startDraw(draw.graphicsContext, info);
                            break;
                        }
                        //ведем линию
                        case "m": {
                            String info = value.substring(2);
                            System.out.println("Test/Получил: " + info);
                            if (!connection.isCommander) draw.draw(draw.graphicsContext, info);
                            break;
                        }
                        //чистим поле
                        case "c":
                            if (!connection.isCommander) draw.clear();
                            break;
                        default:
                            printMessage(value);
                            break;
                    }
                }else //если ответ верный
                    if(!value.startsWith("#correct:"))
                        printMessage(value);
            } else printMessage(value);


    }


    @Override
    public void onDisconnect(Connection connection) {
//        printMessage("Connection close: " + connection);

    }

    @Override
    public void onException(Connection connection, Exception e) {
//        printMessage("Connection exception: " + e);
    }


    private synchronized void printMessage(String message) {
        Platform.runLater(() -> {
            Draw.txtAreaDisplay.appendText(message + "\n");
        });
    }

    //возможно в будущем добавлю вывод имени угадавшего
    private void theEnd(){
    }
}

