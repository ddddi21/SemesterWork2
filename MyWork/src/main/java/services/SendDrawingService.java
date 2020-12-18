package services;


import javafx.scene.paint.Color;
import network.Connection;


import java.util.ArrayList;

public class SendDrawingService {
    public ArrayList<Connection> playersArrayList = new ArrayList<>();
    Connection commander;
    ArrayList <Connection> allPlayers = new ArrayList<>();
    StringBuilder gettingFromCommander;
    StringBuilder sendingToPlayers = new StringBuilder();
    Boolean isStart= false;


    //получаем игроков, которые угадывают
    public void getPlayers(Connection connection){
        playersArrayList.add(connection);
    }

    public void getCommander(Connection connection){
        this.commander = connection;
    }

    public void getAllPlayers(Connection connection){
        this.allPlayers.add(connection);
    }

    public void receivePictureFromCommander(double x, double y, Color colorPicker){

            gettingFromCommander = new StringBuilder();
            gettingFromCommander.append(x).append("#").append(y).append("#").append(colorPicker);
            processingPicture(gettingFromCommander);
    }

    public StringBuilder sendPictureToPlayers(){
        return sendingToPlayers;
    }

    public void processingPicture(StringBuilder stringBuilder){
        sendingToPlayers = stringBuilder;
    }

//    public void fjkdjn(Integer drawId,ArrayList<Connection> playersArrayList ){
//        for (int i = 0; i < playersArrayList.size()-1; i++) {
//            drawId
//        }
//    }
    public void isStartGame(Boolean start){
        this.isStart = start;
    }

    public boolean sendIsStartGame(){
        return isStart;
    }
    //отправлю кому надо что начал рисовать





//        public synchronized void drawToAll(double x, double y, GraphicsContext graphicsContext, Draw draw) {
//        for(Connection connection: connectionArrayList) {
//            connection.getPicture();
//        }
//    }

}
