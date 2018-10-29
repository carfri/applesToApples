package main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private ServerConnection connection;
    private ArrayList<ServerConnection> connections = new ArrayList<ServerConnection>();
    private boolean shouldRun = true;
    private SetUpGame gameInstance;



    public Server(int port, SetUpGame gameInstance){
        try{
            this.gameInstance = gameInstance;
            this.serverSocket = new ServerSocket(port);
            while (connections.size() < gameInstance.playerAmount){
                this.socket = serverSocket.accept();
                this.connection = new ServerConnection(socket, this);
                connection.start();
                connections.add(connection);
                System.out.println("Connection established with client");
            }
            new GameState(this);
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public ArrayList<ServerConnection> getConnections(){
        return connections;
    }

    public SetUpGame getGameInstance(){
        return gameInstance;
    }


}
