package main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.util.Random;

public class SetUpGame {
    private ArrayList<String> greenApples = new ArrayList<String>();
    private ArrayList<String> redApples = new ArrayList<String>();
    private Random randomGenerator;

    public SetUpGame(Integer playerAmount)throws FileNotFoundException{
        Player[] thePlayers = new Player[playerAmount];
        for(int i = 0; i < playerAmount; i++){
            thePlayers[i] = new Player(i);
        }
        loadRedApples();
        loadGreenApples();
        dealToPlayers(thePlayers);
        determineJudge(thePlayers);
       /* for(int i = 0; i < playerAmount; i++){
            System.out.println(thePlayers[i].getHand());
            System.out.println(thePlayers[i].getJudge());
        }*/

    }

    public ArrayList<String> getGreenApples() {
        return greenApples;
    }

    public void setGreenApples(ArrayList<String> greenApples){
        this.greenApples = greenApples;
    }

    private void loadRedApples() throws FileNotFoundException{
        File redFile = new File("redApples.txt");
        Scanner reader = new Scanner(redFile);
        while (reader.hasNextLine())
            redApples.add(reader.nextLine());
    }

    private void loadGreenApples() throws FileNotFoundException{
        File greenFile = new File("greenApples.txt");
        Scanner reader = new Scanner(greenFile);
        while (reader.hasNextLine())
            greenApples.add(reader.nextLine());
    }

    private ArrayList<String> generateInitialHand(){
        ArrayList<String> hand = new ArrayList<String>();
        randomGenerator = new Random();
        for (int i = 0; i < 7; i++){
            int index = randomGenerator.nextInt(redApples.size());
            hand.add(redApples.get(index));
            redApples.remove(index);
        }
        return hand;

    }

    private void dealToPlayers(Player[] thePlayers){
        for (int i = 0; i < thePlayers.length; i++){
            thePlayers[i].setHand(generateInitialHand());
        }
    }

    private void determineJudge(Player[] thePlayers){
        randomGenerator = new Random();
        int index = randomGenerator.nextInt(thePlayers.length);
        thePlayers[index].setJudge();
    }
}
