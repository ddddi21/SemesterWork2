package services;

import network.Connection;

import java.util.ArrayList;

public class UserService {
    public ArrayList<Connection> playersArrayList = new ArrayList<>();
    public Connection commander;
    public ArrayList <Connection> allPlayers = new ArrayList<>();
    

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

//    public String getGuessWord() {
//        WordService wordService;
//        return wordService.randomChoosing();;
//    }
}
