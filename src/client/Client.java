package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
* The client, much like the server is split into two parts. One object listens for System input while the other manages server communication.
* This separation of functionality makes it easier to add additional functionality to the client side.
* For this assignment the ip-address have been hard coded to localhost.
* Can easily be changed to accept server address as user input from the main method when the client is executed.
* This would enable the server to run from a remote location and clients could be located anywhere. The game wouldn't have to be run locally.
* The client doesn't have access to game variables and can therefor not cheat.
* */
public class Client {
    private ClientConnection connection;

    public static void main(String[] args){
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter the port number of the server: ");
        int p = fetchUserInput(reader);
        /*
        System.out.println("Please enter the server address: ");
        int ip = fetchUserInput(reader);
         */
        new Client(p);
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

    private static int fetchUserInput(Scanner reader){
        try{
            while (!reader.hasNextInt()){
                reader.next();
                System.out.println("Input needs to be an Integer! Try again: ");
            }
            int input = reader.nextInt();
            return input;
        }catch (InputMismatchException exception){
            System.out.println("Invalid input!");
            return fetchUserInput(reader);
        }
    }

}
