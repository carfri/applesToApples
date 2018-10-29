package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private ClientConnection connection;

    public static void main(String[] args){
        new Client(3333);
    }
    public Client(int port){
        try{
            Socket socket = new Socket("localhost", port);
            connection = new ClientConnection(socket, this);
            connection.start();
            listenForInput();
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    private void listenForInput(){
        Scanner userInput = new Scanner(System.in);
        while (true){
            while (!userInput.hasNextLine()){
                try {
                    Thread.sleep(1);
                }catch(InterruptedException exception){
                    exception.printStackTrace();
                }
            }
            String input = userInput.nextLine();
            if (input.toLowerCase().equals("quit")){
                break;
            }
            connection.sendToServer(input);

        }
        connection.close();
    }
}
