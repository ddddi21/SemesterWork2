package server;


import javafx.application.Application;
import javafx.stage.Stage;
import network.Connection;
import network.ConnectionListener;
import room.Room;
import services.SendDrawingService;
import services.WordService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;


public class Server extends Application implements ConnectionListener{
    public static final ArrayList<Connection> connectionArrayList = new ArrayList<>();
   SendDrawingService drawingService = new SendDrawingService();
    private Room currentRoom;
    public static String guessWord = "";
    public Connection commander;
    String[] text;
    WordService wordService = new WordService();
    Boolean isWin = false;


    @Override
    public void start(Stage primaryStage) {
       startServer();
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
        System.out.println("список коннекшенов:" + connectionArrayList.toString()+"%");
        //первый подключенный игрок становится ведущим
        if (connectionArrayList.get(0) == connection) {
            drawingService.getCommander(connection);
            System.out.println("Ведущий: "+ connection);
                try{
                    connection.out.write("StartFirst\n");
                    connection.out.flush();
                } catch (IOException e){
                    onException(connection,e);
                }
            } else {
            drawingService.getPlayers(connection);
            sendToAll("new player come in!");

        }


//        sendToAll("Now connected:" + connection);
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String value) {
        System.out.println("Server/Получил: "+value );
        if(!value.equals("null")) {
            if (value.startsWith("#a")) {
                isWin = wordService.isRightWord(value, guessWord);
                value = value.substring(2);
            }
            if(isWin){
                try {
                    connection.out.write("win\n");
                    connection.out.flush();
                } catch (IOException e) {
                    onException(connection,e);
                }
                sendToAll("Игра окончена!" + "\n");
            } else {
                if (value.startsWith("#correct")) {
                    guessWord = value.split(":")[1];
                }
                else sendToAll(value);
            }

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


    public void startServer(){
        try(ServerSocket socket = new ServerSocket(54181)){
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
}