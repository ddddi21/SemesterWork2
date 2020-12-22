package services;

import javafx.scene.paint.Color;
import network.Connection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SendDrawingServiceTest {
    private static Socket socket;
    private static ArrayList<Connection> list;
    StringBuilder gettingFromCommander;
    StringBuilder sendingToPlayers = new StringBuilder();
    SendDrawingService send;
    private BufferedReader in;
    private PrintWriter out;

    @Test
    void getPlayers() {
        list = new ArrayList<>();
//        Connection connection = new Connection(Connec);
    }

    @Test
    void getCommander() {
    }

    @Test
    void getAllPlayers() {
    }


    private void setFlow(Socket socket) throws Exception{
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
    }
}