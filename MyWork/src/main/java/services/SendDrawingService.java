package services;


import javafx.scene.paint.Color;
import network.Connection;


import java.util.ArrayList;

public class SendDrawingService {
    StringBuilder gettingFromCommander;
    StringBuilder sendingToPlayers = new StringBuilder();
    Boolean isStart= false;




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
