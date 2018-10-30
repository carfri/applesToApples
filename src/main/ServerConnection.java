package main;

import java.io.*;
import java.net.Socket;

public class ServerConnection extends Thread {
    private Socket socket;
    private Server server;
    public Player player;
    private boolean acceptInput = false;
    private BufferedReader reader;
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
                    while (din.available() == 0){
                        try {
                            Thread.sleep(1);
                        }catch (InterruptedException exception){
                            exception.printStackTrace();
                        }
                    }
                    String test = din.readUTF();
                    System.out.println(test);
                    if (isValidInput(test) == true){
                        setAcceptInput(false);
                        sendToClient("You've chosen card number" + "[" + test + "]");
                        player.setChoice(Integer.parseInt(test));
                        System.out.println(acceptInput);
                    }
                    else if (isValidInput(test) == false){
                        sendToClient("your input is invalid! Try again:");
                    }
                }
                else if (acceptInput == false){
                    try {
                        Thread.sleep(1);
                    }catch (InterruptedException exception){
                        exception.printStackTrace();
                    }
                }
                //String message = din.readUTF();
                //System.out.println(message);
            }
            din.close();
            dout.close();
            socket.close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    private boolean isValidInput(String test){
        boolean status;
        int choice;
        try{
            choice = Integer.parseInt(test);
            if (choice >= 0 && choice <= 6){
                status = true;
            }
            else{
                status = false;
            }
        }catch(NumberFormatException exception){
            status = false;
        }
        return status;
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
