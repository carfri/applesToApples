package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private ClientConnection connection;

    public static void main(String[] args){
        new Client(6666);
    }
    public Client(int port){
        try{
            Socket socket = new Socket("localhost", port);
            connection = new ClientConnection(socket, this);
            connection.start();
            listenForInput1();
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    /*private void listenForInput(){
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
    }*/

    private void listenForInput1(){
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            try {
                String line = userInput.readLine();
                connection.sendToServer(line);
            }catch (IOException exception){
                exception.printStackTrace();
            }
        }
    }
}
