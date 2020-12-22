import javafx.stage.Stage;
import network.Connection;
import network.ConnectionListener;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class GameTest {
    private static Socket socket;
    private static ConnectionListener connectionListener;
    private static Connection connection;
    private static Stage stage;
    private static Server server;
    private static ArrayList<Connection> list;
    private BufferedReader in;
    private PrintWriter out;

    @BeforeEach
    public void startServer() throws Exception{
//        if(server != null) server.onDisconnect(c);
        server = new Server();

        server.startServer();
        Thread.sleep(100);
    }
    @AfterEach
    public void disconnect() throws Exception{
        if(socket != null){
            socket.close();
        }
        socket = null;
        Thread.sleep(100);
    }

    @Test
    public void onConnectionReady(Connection connection) {
        server.onConnectionReady(connection);
        ArrayList<Connection> list = new ArrayList<>();
        list.add(connection);
        Assertions.assertEquals(Server.connectionArrayList.get(0), list.get(0));


    }

    private void createConnectionListenerForServer(){
        connectionListener = new ConnectionListener() {

            @Test
            @Override
            public void onConnectionReady(Connection connection) {
                server = new Server();
                server.onConnectionReady(connection);
                ArrayList<Connection> list = new ArrayList<>();
                list.add(connection);
                Assertions.assertEquals(Server.connectionArrayList.get(0), list.get(0));


            }

            @Override
            public void onReceiveString(Connection connection, String value) {

            }

            @Override
            public void onDisconnect(Connection connection) {

            }

            @Override
            public void onException(Connection connection, Exception e) {

            }
        };
    }

    @Test
    void connectionCreate() throws Exception{
        socket = new Socket("localhost",54181);
        Connection con = new Connection(connectionListener,socket);
        Connection con1 = Server.connectionArrayList.get(0);
        Assertions.assertEquals(con1, con);
    }

//    @BeforeAll
//    static void ServerOnConnectionReady(Connection connection) {
//        try {
//            connection = new Connection(connectionListener, new Socket("localhost", 66181));
//        } catch (IOException e) {
//            throw new IllegalArgumentException(e);
//        }
//        Connection
//    }

    private Socket createSocket() throws Exception{
        socket = new Socket("localhost",54181);
        Thread.sleep(200);
        return socket;
    }
    private void setFlow(Socket socket) throws Exception{
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
    }




}
