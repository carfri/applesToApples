package main;
/*
 * Author: Carl Frisenstam. carl.frisenstam@gmail.com
 * */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
* The server is split into 2 parts. One server class and one serverConnection class.
* The server class tracks and accepts client connections.
* When a new connection is established a ServerConnection object is initialized.
* ServerConnection establishes an input and output stream which listens to a specific socket.
* Removes any need to identify specific sockets since the server connection will always be linked to one unique socket.
*
* When all connections are established, the server initializes a gameState object.
* GameState is where the gameLoop executes
* */

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private ServerConnection connection;
    private ArrayList<ServerConnection> connections = new ArrayList<ServerConnection>();
    private GameState gameState;
    private int counter = 0;
    private SetUpGame gameInstance;



    public Server(int port, SetUpGame gameInstance){
        try{
            this.gameInstance = gameInstance;
            this.serverSocket = new ServerSocket(port);
            while (connections.size() < gameInstance.playerAmount){ //Specifies how many connections the server is set to wait for.
                this.socket = serverSocket.accept();
                this.connection = new ServerConnection(socket, counter, this);
                connection.start();
                counter ++;
                connections.add(connection);
                System.out.println("Connection established with client");
            }
            this.gameState = new GameState(this);
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public ArrayList<ServerConnection> getConnections(){
        return connections;
    }

    public GameState getGameState(){
        return gameState;
    }

    public SetUpGame getGameInstance(){
        return gameInstance;
    }



}
