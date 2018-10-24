package main;

import java.util.ArrayList;
import java.util.Random;

public class GameState {
    private Random randomGenerator;
    private SetUpGame gameInstance;

    public GameState(SetUpGame gameInstance){
        this.gameInstance = gameInstance;
    }

    public void gameLoop(){
        System.out.println("Pair a red apple with this green:\n " + drawGreen());
    }

    private String drawGreen(){
        randomGenerator = new Random();
        ArrayList<String> greenDeck = gameInstance.getGreenApples();
        int index = randomGenerator.nextInt(greenDeck.size());
        String drawnCard = greenDeck.get(index);
        greenDeck.remove(index);
        gameInstance.setGreenApples(greenDeck);
        return drawnCard;
    }

    private void gatherRedApples(){

    }


}
