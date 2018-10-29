package main;

import java.util.ArrayList;
import java.util.Random;

public class GameState {
    private Random randomGenerator;
    private Server server;

    public GameState(Server server){
        this.server = server;
        dealToPlayers(server.getConnections());
        gameLoop();
    }

    public void gameLoop(){
        collectRedApples();
        collectVerdict();
        distributeScore();
    }

    private String drawGreen(){
        randomGenerator = new Random();
        ArrayList<String> greenDeck = server.getGameInstance().getGreenApples();
        int index = randomGenerator.nextInt(greenDeck.size());
        String drawnCard = greenDeck.get(index);
        greenDeck.remove(index);
        server.getGameInstance().setGreenApples(greenDeck);
        return drawnCard;
    }

    private void collectRedApples(){
        System.out.println("Asking clients for input...");
        String greenCard = drawGreen();
        for (int i = 0; i < server.getConnections().size();i++ ){
            server.getConnections().get(i).sendToClient("Pair a red apple with this green:\n " + greenCard + "\n");
            server.getConnections().get(i).sendToClient("Your hand of red apples:\n ");
            server.getConnections().get(i).sendCurrentHand();
            server.getConnections().get(i).setAcceptInput(true);
        }
    }

    private boolean collectVerdict(){
        return true;
    }

    private void distributeScore(){

    }

    private ArrayList<String> generateInitialHand(){
        ArrayList<String> redApples = server.getGameInstance().getRedApples();
        ArrayList<String> hand = new ArrayList<String>();
        randomGenerator = new Random();
        for (int i = 0; i < 7; i++){
            int index = randomGenerator.nextInt(redApples.size());
            hand.add(redApples.get(index));
            redApples.remove(index);
        }
        server.getGameInstance().setRedApples(redApples);
        return hand;
    }

    private void dealToPlayers(ArrayList<ServerConnection> thePlayers){
        for (int i = 0; i < thePlayers.size(); i++){
            thePlayers.get(i).player.setHand(generateInitialHand());
        }


    }



}
