package main;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Player {
    private boolean isBot;
    private boolean isJudge;
    private int choice;
    private Socket connection;
    private ArrayList<String> hand;
    private Random randomGenerator;
    private SetUpGame gameInstance;

    public Player(){
        this.isBot = false;
        this.isJudge = false;
    }

    public void setHand(ArrayList<String> hand) {
        this.hand = hand;
    }

    public ArrayList<String> getHand(){
        return this.hand;
    }

    public void setJudge(){
        this.isJudge = true;
    }

    public boolean getJudge(){
        return this.isJudge;
    }

    private void addCard(String card){

    }

    private void removeCard(){

    }

    /*private void generateInitialHand(){
        ArrayList<String> redApples = gameInstance.getRedApples();
        randomGenerator = new Random();
        System.out.println("hihihi");
        for (int i = 0; i < 7; i++){
            int index = randomGenerator.nextInt(redApples.size());
            hand.add(redApples.get(index));
            redApples.remove(index);
        }
        gameInstance.setRedApples(redApples);
    }*/

    public void setChoice(int choice){
        this.choice = choice;
    }
}
