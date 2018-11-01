package main;

import java.io.*;
import java.net.Socket;
/*
* The serverConnection is tied to a unique clientConnection.
* Holds flag variables to check whether or not the game loop requires input from certain clients.
* Overwrites the standard run method to listen for changes in the output and input stream.
* When an input from client is detected it will check for status on specific flags and accept relevant input.
*
* */
public class ServerConnection extends Thread {
    private Socket socket;
    private Server server;
    public Player player;
    private boolean acceptInput = false;
    private DataInputStream din;
    private DataOutputStream dout;
    private boolean shouldRun = true;

    public ServerConnection(Socket socket,int counter, Server server){
        super("Server connection thread");
        this.socket = socket;
        this.server = server;
        player = new Player(false, counter);
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
            sendToClient("Established connection to server. Please wait for all players to join. \n");

            while (shouldRun){
                if (player.getJudge() == true && acceptInput == true){

                    while (din.available() == 0){
                        try {
                            Thread.sleep(1);
                        }catch (InterruptedException exception){
                            exception.printStackTrace();
                        }
                    }
                    String judgeInput = din.readUTF();
                    System.out.println(judgeInput);
                    if (isValidJudgeInput(judgeInput) == true){
                        setAcceptInput(false);
                        sendToClient("You've selected player [" + judgeInput + "] to win this round.");
                        player.setJudgeChoice(Integer.parseInt(judgeInput));
                        System.out.println("Judge has selected player [" + player.getJudgeChoice() + "] to win this round");
                    }
                    else if (isValidJudgeInput(judgeInput) == false){
                        sendToClient("Your input is invalid! Try again:");
                    }
                }
                if (player.getJudge() == false && acceptInput == true){
                    while (din.available() == 0){
                        try {
                            Thread.sleep(1);
                        }catch (InterruptedException exception){
                            exception.printStackTrace();
                        }
                    }
                    String userInput = din.readUTF();
                    System.out.println(userInput);
                    if (isValidUserInput(userInput) == true){
                        setAcceptInput(false);
                        sendToClient("You've chosen card number" + "[" + userInput + "]");
                        player.setChoice(Integer.parseInt(userInput));
                        player.playCard(player.getChoice());
                        System.out.println("A player have played card [" + player.getChoice() + "]");
                    }
                    else if (isValidUserInput(userInput) == false){
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
            }
            close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    private boolean isValidUserInput(String test){
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

    private boolean isValidJudgeInput(String test){
        boolean status;
        int choice;
        try{
            choice = Integer.parseInt(test);
            if (choice >= 0 && choice < server.getGameInstance().getTotalPlayerAmount()){
                System.out.println("lalalalaal");
                if (choice == player.id){
                    System.out.println("yoyoyo you judged");
                    return false;
                }
                status = true;
            }
            else{
                status = false;
            }
        }catch(NumberFormatException exception){
            status = false;
        }
        return status;
        //server.getConnections().get(choice).player.getJudge() == true
    }

    public void sendCurrentHand(){
        for (int i = 0; i < 7; i++){
            sendToClient("["+i+"]   " +player.getHand().get(i));
        }
    }

    public void setAcceptInput(boolean flag){
        this.acceptInput = flag;
    }

    public boolean getAcceptInput(){
        return this.acceptInput;
    }

    public void close(){
        try {
            din.close();
            dout.close();
            socket.close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }


}
