package main;
/*
* Author: Carl Frisenstam. carl.frisenstam@gmail.com
* */
import java.util.InputMismatchException;
import java.util.Scanner;

public class ApplesToApples {
    /*
    * Main function takes 3 inputs as game modifiers. How many players? How many bots? What port number to lissten for client connections?
    * Handles and verifies that any user input is valid.
    * Sets up a new game
    * Initilizes server. The game loop is run server side and is started after all clients are connected.
    * */
    public static void main(String [] args){
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter the number of players: ");
        int n = fetchUserInput(reader);
        System.out.println("Please enter the number of bots, if no bots are wanted type 0: ");
        int b = fetchUserInput(reader);
        System.out.println("Please enter the port for server - client communication: ");
        int p = fetchUserInput(reader);
        reader.close();
        SetUpGame gameInstance = new SetUpGame(n, b);
        new Server(p, gameInstance);
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

