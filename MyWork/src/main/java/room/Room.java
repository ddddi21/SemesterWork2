package room;

import network.Connection;

import java.util.ArrayList;
import java.util.List;

public class Room {
    public List<Connection> list = new ArrayList<>();
    public int number;

    public Room(int number){
        this.number = number;
    }
}