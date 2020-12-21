package network;



import room.Room;
import services.SendDrawingService;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Connection {
    ConnectionListener connectionListener;
    public Socket socket;
    private Thread thread;
    boolean isDraw = false;
    private final List<Room> rooms = new ArrayList<>();
    public Boolean isCommander = false;
    private BufferedReader in;
    public BufferedWriter out;
    public Integer id;
    public String guessWord;


    public Connection(ConnectionListener connectionListener, String ip, Integer port) throws IOException{
        this(connectionListener, new Socket(ip,port));
    }

    public Connection(ConnectionListener connectionListener, Socket socket) throws IOException {
        this.connectionListener = connectionListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connectionListener.onConnectionReady(Connection.this);
                    while (!thread.isInterrupted()){
                        System.out.println("Connection before receive");
                        connectionListener.onReceiveString(Connection.this, Connection.this.in.readLine());
                        System.out.println("Connection receive");
                    }
                } catch (IOException e) {
                    connectionListener.onException(Connection.this,e);
                }
                finally {
                    connectionListener.onDisconnect(Connection.this);
                }
            }
        });
        thread.start();
    }

    public synchronized void sendString(String string){
        try {
            System.out.println("Connection/Отправил: "+string);
            out.write(string + "\r\n");
            out.flush();
        } catch (IOException e) {
            connectionListener.onException(Connection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect(){
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            connectionListener.onException(Connection.this, e);
        }
    }
    public Room createRoom(int number){
        Room room = new Room(number);
        rooms.add(room);
        return room;
    }

    public Room connectToRoom(int number){
        for(Room room:rooms){
            if(room.number == number) return room;
        }
        return null;
    }
    public boolean roomIsCreat(int number){
        boolean isCreate = false;
        for(Room room:rooms){
            if(room.number == number)isCreate = true;
        }
        return isCreate;
    }
    @Override
    public String toString(){
        return "Connection" + socket.getInetAddress() + ": " + socket.getPort();
    }
}