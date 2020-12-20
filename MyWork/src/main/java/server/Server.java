package server;


import javafx.application.Application;
import javafx.stage.Stage;
import network.Connection;
import network.ConnectionListener;
import room.Room;
import services.SendDrawingService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;


public class Server extends Application implements ConnectionListener{
    public static final ArrayList<Connection> connectionArrayList = new ArrayList<>();
/*    private final List<Room> rooms = new ArrayList<>();*/   //Нам ето не нужон
   SendDrawingService drawingService = new SendDrawingService();
    private Room currentRoom;


    @Override
    public void start(Stage primaryStage) {
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


    public void sendToAll(String string){
        for (Connection connection: connectionArrayList) {
            connection.sendString(string);
        }
    }

}