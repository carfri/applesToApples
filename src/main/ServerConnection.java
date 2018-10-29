package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Thread {
    private Socket socket;
    private Server server;
    public Player player;
    public boolean acceptInput = false;
    private DataInputStream din;
    private DataOutputStream dout;
    private boolean shouldRun = true;

    public ServerConnection(Socket socket, Server server){
        super("Server connection thread");
        this.socket = socket;
        this.server = server;
        player = new Player();
    }

    public void sendToClient(String data){
        try {
            dout.writeUTF(data);
            dout.flush();
        }catch (IOException exception){
            exception.printStackTrace();
        }

    }

    public void sendToAllClients(String data){
        for (int i = 0; i < server.getConnections().size(); i++){
            ServerConnection sc = server.getConnections().get(i);
            sc.sendToClient(data);
        }
    }

    public void run(){
        try {
            this.din = new DataInputStream(socket.getInputStream());
            this.dout = new DataOutputStream(socket.getOutputStream());

            while (shouldRun){
                if (acceptInput == true){
                    sendToClient("Select your apple:");
                    while (din.available() == 0){
                        try {
                            Thread.sleep(1);
                        }catch (InterruptedException exception){
                            exception.printStackTrace();
                        }
                    }
                    try {
                        int choice = Integer.parseInt(din.readUTF());
                    }catch (NumberFormatException exception){
                        sendToClient("please input a valid number");
                    }
                }

                String textIn = din.readUTF();
                sendToAllClients(textIn);
            }
            din.close();
            dout.close();
            socket.close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void sendCurrentHand(){
        for (int i = 0; i < 7; i++){
            sendToClient("["+i+"]   " +player.getHand().get(i));
        }
    }

    public void setAcceptInput(boolean flag){
        this.acceptInput = flag;
    }
}
