package main;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class ApplesToApples {
    public static void main(String [] args)throws FileNotFoundException {
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter the number of players: ");
        int n = reader.nextInt();
        reader.close();
        SetUpGame gameInstance = new SetUpGame(n);
        GameState board = new GameState(gameInstance);
        board.gameLoop();

    }
}
