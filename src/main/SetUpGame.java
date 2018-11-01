package main;
/*
 * Author: Carl Frisenstam. carl.frisenstam@gmail.com
 * */

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.Collections;
import java.util.Scanner;
/*
* This class sets up very basic game parameters. Bots are generated for use later in the game loop.
* The decks of red and green apples are loaded in from textfiles.
* The decks are shuffled, everytime a new card is drawn the first element of the deck is drawn and then removed.
* */
public class SetUpGame {
    private ArrayList<String> greenApples = new ArrayList<String>();
    private ArrayList<String> redApples = new ArrayList<String>();
    private ArrayList<Player> botContainer = new ArrayList<Player>();
    public int playerAmount;
    public int botAmount;

    public SetUpGame(Integer playerAmount, Integer botAmount){
        this.playerAmount = playerAmount;
        this.botAmount = botAmount;
        loadRedApples();
        loadGreenApples();
        Collections.shuffle(greenApples);
        Collections.shuffle(redApples);
        generateBots();
    }

    public ArrayList<String> getGreenApples() {
        return greenApples;
    }

    public void setGreenApples(ArrayList<String> greenApples){
        this.greenApples = greenApples;
    }

    public ArrayList<String> getRedApples(){
        return redApples;
    }

    public void setRedApples(ArrayList<String> redApples){
        this.redApples = redApples;
    }

    private void generateBots(){
        int counter = playerAmount;
        for (int i = 0; i < botAmount; i++){
            botContainer.add(new Player(true, counter));
            counter ++;
        }
    }

    public ArrayList<Player> getBotContainer(){
        return this.botContainer;
    }

    private void loadRedApples(){
        try{
            File redFile = new File("redApples.txt");
            Scanner reader = new Scanner(redFile);
            while (reader.hasNextLine())
                redApples.add(reader.nextLine());
        }catch (FileNotFoundException exception){
            System.out.println("Cannot find redApples.txt");
        }
    }

    private void loadGreenApples(){
        try {
            File greenFile = new File("greenApples.txt");
            Scanner reader = new Scanner(greenFile);
            while (reader.hasNextLine())
                greenApples.add(reader.nextLine());
        }catch (FileNotFoundException exception){
            System.out.println("Cannot find greenaApples.txt");
        }
    }

    public void drawRedApple(){
        redApples.remove(0);
    }

    public void drawGreenApple(){
        greenApples.remove(0);
    }

    public void addRedApple(String card){
        redApples.add(card);
    }

    public int getTotalPlayerAmount(){
        return playerAmount + botAmount;
    }


}
