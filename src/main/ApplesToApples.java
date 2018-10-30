package main;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ApplesToApples {
    public static void main(String [] args)throws Exception {
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter the number of players: ");
        int n = fetchUserInput(reader);
        System.out.println("Please enter the port for server - client communication: ");
        int p = fetchUserInput(reader);
        reader.close();
        SetUpGame gameInstance = new SetUpGame(n);
        new Server(p, gameInstance);
        //GameState board = new GameState(gameInstance, server);
        //board.gameLoop();
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

