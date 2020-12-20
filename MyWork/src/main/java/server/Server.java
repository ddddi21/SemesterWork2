package server;


import drawing.Draw;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Test;
import network.Connection;
import network.ConnectionListener;
import room.Room;
import services.SendDrawingService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server extends Application implements ConnectionListener{
    /*public TextArea txtAreaDisplay;*/ //Нам ето не нужон
    public static final ArrayList<Connection> connectionArrayList = new ArrayList<>();
/*    private final List<Room> rooms = new ArrayList<>();*/   //Нам ето не нужон
    public static Boolean isHasCommander = false;
   SendDrawingService drawingService = new SendDrawingService();
//    private Client2 client = new Client2();
/*    private Test client = new Test();*/       //Нам ето не нужон
    private Room currentRoom;


    @Override
    public void start(Stage primaryStage) throws Exception {
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
        connection.id = connectionArrayList.indexOf(connection);
        drawingService.getAllPlayers(connection);
        //первый подключенный игрок становится ведущим
        if (connectionArrayList.get(0) == connection) {
            drawingService.getCommander(connection);
                try{
                    connection.out.write("StartFirst\n");
                    connection.out.flush();
                } catch (IOException e){
                    e.printStackTrace();
                }
            } else {
            drawingService.getPlayers(connection);
        }


//        sendToAll("Now connected:" + connection);
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String value) {
        System.out.println("Server/Получил: "+value );
        if(!value.equals("null")) {
            sendToAll(value);
/*            Platform.runLater(() -> {
                txtAreaDisplay.appendText(value + "\n");
            });*/           //Нам ето не нужон
        }
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connectionArrayList.remove(connection);
        sendToAll("Now disconnected =( :" + connection);
    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.out.println("Connection exception: " + e);
    }

    //передаю в коннекшн что начал рисвать
    @Override
    public void onStartDrawing(Connection connection, boolean isStart) {
        System.out.println("Server: Начал рисовать");
        if(isStart){
            try {
                connection.out.write("GameIsStarting");
                connection.out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToAll(String string){
        for (Connection connection: connectionArrayList) {
            connection.sendString(string);
        }
    }

}