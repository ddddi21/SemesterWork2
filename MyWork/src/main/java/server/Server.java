package server;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import main.Test;
import network.Connection;
import network.ConnectionListener;
import room.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server extends Application implements ConnectionListener{
    public TextArea txtAreaDisplay;
    public static final ArrayList<Connection> connectionArrayList = new ArrayList<>();
//    private Client2 client = new Client2();
    private Test client = new Test();
    private Room currentRoom;
    private final List<Room> rooms = new ArrayList<>();
    public static Boolean isHasCommander = false;


    @Override
    public void start(Stage primaryStage) throws Exception {
        txtAreaDisplay = new TextArea();
        txtAreaDisplay.setEditable(false);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(txtAreaDisplay);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 450, 500);
        primaryStage.setTitle("Server: JavaFx Text Chat App");
        primaryStage.setScene(scene);
        primaryStage.show();

        Platform.runLater(()
                -> txtAreaDisplay.appendText("New server started at " + new Date() + '\n'));
        try(ServerSocket socket = new ServerSocket(8181)){
            while(true){
                try {
                    new Connection(this, socket.accept());
                } catch (IOException e){
                    System.out.println("Connection exception: " + e);
                }
            }
        } catch (IOException e){
            throw  new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
//        String room = client.roomField.getText().trim();
//        if(connection.roomIsCreat(Integer.parseInt(room))){
//            currentRoom = connection.connectToRoom(Integer.parseInt(room));
//        } else{
//            currentRoom = connection.createRoom(Integer.parseInt(room));
//        }
//        currentRoom.list.add(connection);
        connectionArrayList.add(connection);
            if (connectionArrayList.get(0) == connection) {
                connection.isCommander = true;
            }
            //потому что вот тут он еще походу не успевает добавить в лист и поэтому isCommander null.
        //но непонятно почему ведь сервер раньше запускается кароче беды беды

        sendToAll("Now connected:" + connection);
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String value) {
        sendToAll(value);
        Platform.runLater(() -> {
            txtAreaDisplay.appendText(value + "\n");
        });
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connectionArrayList.remove(connection);
        sendToAll("Now disconnected =( :" + connection);
    }
    //TODO(не работает шота)

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.out.println("Connection exception: " + e);
    }

    public void sendToAll(String string){
        for (Connection connection: connectionArrayList) {
            connection.sendString(string);
        }
    }

}