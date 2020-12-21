package server;


import javafx.application.Application;
import javafx.stage.Stage;
import main.Client;
import network.Connection;
import network.ConnectionListener;
import room.Room;
import services.SendDrawingService;
import services.UserService;
import services.WordService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;


public class Server extends Application implements ConnectionListener{
    public static final ArrayList<Connection> connectionArrayList = new ArrayList<>();
/*    private final List<Room> rooms = new ArrayList<>();*/   //Нам ето не нужон
   SendDrawingService drawingService = new SendDrawingService();
    private Room currentRoom;
    public static String guessWord = "";
    public Connection commander;
    UserService userService = new UserService();
    String[] text;
    WordService wordService = new WordService();
    Boolean isWin = false;
    private Client client = new Client();



    @Override
    public void start(Stage primaryStage) {
        try(ServerSocket socket = new ServerSocket(7181)){
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
            //нужен фикс
            //подозреваю что баг здесь
            if (value.startsWith("#a")) {
                isWin = wordService.isRightWord(value, guessWord);
                value = value.substring(2);
            }
            if(isWin){
                try {
                    connection.out.write("win\n");
                    connection.out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
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

    //нужен фикс
    public boolean isRightAnswer(String word){
        text = word.split(":");
        guessWord = commander.guessWord;
        boolean guess = false;
        if (guessWord.equals(text[1].trim().toLowerCase())) {
            guess = true;

        } return guess;
    }

}