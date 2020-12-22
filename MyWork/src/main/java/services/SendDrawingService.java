package services;


import javafx.scene.paint.Color;
import network.Connection;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SendDrawingService {
    public ArrayList<Connection> playersArrayList = new ArrayList<>();
    Connection commander;
    Socket thisSocket;
    BufferedReader in;
    PrintWriter out;
    ArrayList <Connection> allPlayers = new ArrayList<>();
    StringBuilder gettingFromCommander;
    StringBuilder sendingToPlayers = new StringBuilder();


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
    public void setSocket(Socket socket){
        thisSocket = socket;
        System.out.println("DrawService socket: "+ socket);
        try {
            in = new BufferedReader(new InputStreamReader(thisSocket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(thisSocket.getOutputStream(),StandardCharsets.UTF_8), true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void receivePictureFromCommander(double x, double y, Color colorPicker){

            gettingFromCommander = new StringBuilder();
            gettingFromCommander.append(x).append("#").append(y).append("#").append(colorPicker);
            processingPicture(gettingFromCommander);
    }

    public void sendPictureToPlayers(){
        out.println("#m"+sendingToPlayers.toString());
        out.flush();
    }

    public void sendStartLine(){
        out.println("#s"+sendingToPlayers.toString());
        out.flush();
    }

    public void processingPicture(StringBuilder stringBuilder){
        sendingToPlayers = stringBuilder;
    }

    public void cleanBoard() {
        out.println("#c");
        out.flush();
    }


    public void sendCorrectAnswerToServer(String guesstionWord){
        out.println("#correct:" + guesstionWord);
        out.flush();
    }
}
