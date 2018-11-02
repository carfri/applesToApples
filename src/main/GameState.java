package main;
/*
 * Author: Carl Frisenstam. carl.frisenstam@gmail.com
 * */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/*
* This class handles the game loop.
* The loop runs multiple methods that handles game execution and sets the rules for the game.
* The methods can easily be rewritten to accommodate rule changes.
* Additional methods can be added to increase the game functionality
* For example if you want to extend judge privileges for each run to all players
* The determine judge method could be changed to setJudge true for all participants
* */
public class GameState {
    private Random randomGenerator;
    private Server server;
    private ArrayList<ServerConnection> connections;
    private ArrayList<Player> thePlayers = new ArrayList<Player>();
    private ServerConnection judge;
    private ArrayList<String> playedCards = new ArrayList<String>();

    public GameState(Server server){
        this.server = server;
        this.connections = server.getConnections();
        this.thePlayers = generatePlayersList();
        System.out.println(thePlayers);
        dealToPlayers();
        gameLoop();
    }

    private ArrayList<Player> generatePlayersList(){
        for (int i = 0; i < connections.size(); i++){
            thePlayers.add(connections.get(i).player);
        }
        for (int i = 0; i < server.getGameInstance().botAmount; i++){
            thePlayers.add(server.getGameInstance().getBotContainer().get(i));
        }
        return thePlayers;
    }

    public void gameLoop(){
        while (true){
            resetPlayedCards();
            resetJudge();
            resetChoice();
            determineJudge();
            askPlayersForRedApples();
            waitForAllPlayers();
            askBotsForRedApples();
            showPlayedApples();
            for (int i = 0; i < connections.size(); i++){           //Checks whether or not a player is judge
                if (connections.get(i).player.getJudge() == true){
                    judge = connections.get(i);
                    System.out.println("Player is judging");
                    collectPlayerVerdict();
                    waitForJudge();
                    break;
                }
                else{
                    System.out.println("Bot is judging");
                    collectBotVerdict();
                }
            }
            distributeScore();
            returnPlayedApplesToDeck();
            drawNewCards();
            if (checkForWinner() == true){
                break;
            }
        }
        System.out.println("Thanks for playing friend, come back anytime.");
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

    private void resetPlayedCards(){
        playedCards.clear();
    }

    private void resetJudge(){
        judge = null;
        for (int i = 0; i < thePlayers.size(); i++){
            thePlayers.get(i).setJudge(false);
        }
    }

    private void resetChoice(){
        for (int i = 0; i < thePlayers.size(); i++){
            thePlayers.get(i).setChoice(-1);
        }
    }

    private void determineJudge(){
        randomGenerator = new Random();
        int index = randomGenerator.nextInt(thePlayers.size());
        thePlayers.get(index).setJudge(true);
    }

    private void askPlayersForRedApples(){
        System.out.println("Asking clients for input...");
        String greenCard = drawGreen();
        for (int i = 0; i < connections.size(); i++){
            if (connections.get(i).player.getJudge() == false){
                connections.get(i).sendToClient("Pair a red apple with this green:\n " + greenCard + "\n");
                connections.get(i).sendToClient("Your hand of red apples:\n ");
                connections.get(i).sendCurrentHand();
                connections.get(i).setAcceptInput(true);
                connections.get(i).sendToClient("please select an apple:");
            }
            else{
                connections.get(i).sendToClient("you have been selected judge for this turn, please wait until all the players have selected their red apples.");
                connections.get(i).sendToClient("The green apple drawn for this turn:\n " + greenCard + "\n");
            }
        }
    }

    private void askBotsForRedApples(){
        for (int i = 0; i < server.getGameInstance().botAmount; i++){
            server.getGameInstance().getBotContainer().get(i).playCard(0);
            server.getGameInstance().getBotContainer().get(i).setChoice(0);
        }
    }

    private void waitForAllPlayers(){
        boolean playersSelecting = true;
        int counter = 0;
        while(playersSelecting == true){
            if (connections.size() == 1){
                while (connections.get(0).getAcceptInput() == true){
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    }catch (InterruptedException exception){
                        exception.printStackTrace();
                    }
                }
            }
            //continuisly check if a player is still flaged for input
            for (int i = 0; i < connections.size(); i++){
                if (connections.get(i).getAcceptInput() == true){
                    System.out.println("Still waiting");
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    }catch (InterruptedException exception){
                        exception.printStackTrace();
                    }
                    break;
                }
                counter = i;
            }
            if (counter + 1 == connections.size()){
                playersSelecting = false;
            }
        }
    }

    private void showPlayedApples(){
        for (int i = 0; i < thePlayers.size(); i++) {
            if (thePlayers.get(i).getJudge() == false) {
                playedCards.add(thePlayers.get(i).getPlayedCard());
                System.out.println("added to playedCard deck");
            }
        }
        Collections.shuffle(playedCards);
        for (int i = 0; i < connections.size(); i++){
            connections.get(i).sendToClient("########################################################################################################\n" +
                    "                         These red apples were played this round                     \n");
            for (int p = 0; p < playedCards.size(); p++){
                connections.get(i).sendToClient( "[" + p + "]" + playedCards.get(p)+ "\n");
            }
            connections.get(i).sendToClient("########################################################################################################\n");
        }
        System.out.println("hahaha");
        for (int i = 0; i < playedCards.size(); i++){
        }
    }

    private void collectPlayerVerdict(){
        judge.sendToClient("Please select the combination of apples to win this round.");
        judge.setAcceptInput(true);
    }

    private void collectBotVerdict(){
        randomGenerator = new Random();
        for (int i = 0; i < thePlayers.size(); i++){
            if (thePlayers.get(i).getJudge() == true){
                int index = randomGenerator.nextInt(playedCards.size());
                thePlayers.get(i).setJudgeChoice(index);
            }
        }
    }

    private void waitForJudge(){
        while (judge.getAcceptInput() == true){
            System.out.println("Waiting for verdict");
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            }catch (InterruptedException exception){
                exception.printStackTrace();
            }
        }
    }

    private void distributeScore(){
        for (int i = 0; i < thePlayers.size(); i++){
            if (thePlayers.get(i).getJudge() == true){
                String winningCard = playedCards.get(thePlayers.get(i).getJudgeChoice());
                for (int p = 0; p < thePlayers.size(); p++){
                    if (winningCard.equals(thePlayers.get(p).getPlayedCard())){ //check each player if he holds the winning card
                        thePlayers.get(p).setScore(thePlayers.get(p).getScore() + 1);
                        connections.get(0).sendToAllClients(
                                "##################################################################################\n" +
                                        "#                                                                                #\n" +
                                        "# Player [" + p + "] have been awarded 1 point for winning this round!                   #\n" +
                                        "#                                                                                #\n" +
                                        "##################################################################################");
                    }
                }
            }
        }
    }

    private ArrayList<String> generateInitialHand(){
        ArrayList<String> redApples = server.getGameInstance().getRedApples();
        ArrayList<String> hand = new ArrayList<String>();
        for (int i = 0; i < 7; i++){
            hand.add(redApples.get(0));
            server.getGameInstance().drawRedApple();
        }
        return hand;
    }

    private void dealToPlayers(){
        for (int i = 0; i < thePlayers.size(); i++){
            thePlayers.get(i).setHand(generateInitialHand());
        }
    }

    private void returnPlayedApplesToDeck(){
        ArrayList<String> playedCards = new ArrayList<String>();
        for (int i = 0; i < thePlayers.size(); i++){
            if (thePlayers.get(i).getJudge() == false){
                playedCards.add(thePlayers.get(i).getPlayedCard());
                System.out.println("card in pile");
            }
        }
        Collections.shuffle(playedCards);
        for (int i = 0; i < playedCards.size(); i++){
            server.getGameInstance().addRedApple(playedCards.get(i));
            System.out.println("putting (" + playedCards.get(i)+ ") back in the deck");
        }
    }

    private void drawNewCards(){
        for (int i = 0; i < thePlayers.size(); i++){
            if (thePlayers.get(i).getHand().size() < 7){
                thePlayers.get(i).drawCard(server.getGameInstance().getRedApples().get(0));
                server.getGameInstance().drawRedApple();
            }
        }
    }

    private boolean checkForWinner(){
        if(thePlayers.size() <= 4){
            for (int i = 0; i < thePlayers.size(); i++){
                if (thePlayers.get(i).getScore() == 8){
                    gameWon(i);
                    return true;
                }
            }
        }
        else if (thePlayers.size() == 5){
            for (int i = 0; i < thePlayers.size(); i++){
                if (thePlayers.get(i).getScore() == 7){
                    gameWon(i);
                    return true;
                }
            }
        }
        else if (thePlayers.size() == 6){
            for (int i = 0; i < thePlayers.size(); i++){
                if (thePlayers.get(i).getScore() == 6){
                    gameWon(i);
                    return true;
                }
            }
        }
        else if (thePlayers.size() == 7){
            for (int i = 0; i < thePlayers.size(); i++){
                if (thePlayers.get(i).getScore() == 5){
                    gameWon(i);
                    return true;
                }
            }
        }
        else if (thePlayers.size() >= 8){
            for (int i = 0; i < thePlayers.size(); i++){
                if (thePlayers.get(i).getScore() == 4){
                    gameWon(i);
                    return true;
                }
            }
        }
        return false;


    }

    private void gameWon(int index){
        connections.get(0).sendToAllClients(
                "###############################################################################\n" +
                        "#                                                                            #\n" +
                        "#                                                                            #\n" +
                        "#                             GAME OVER                                      #\n" +
                        "#                   Player [" + index + "] has won the game!                             #\n" +
                        "#                                                                            #\n" +
                        "#                                                                            #\n" +
                        "###############################################################################\n");
        System.out.println("Closing down game...");
        int playerNumbers = connections.size();
        for (int i = 0; i < playerNumbers; i++){
            connections.get(i).close();
        }
    }

    public ArrayList<String> getPlayedCards(){
        return this.playedCards;
    }

    public ArrayList<Player> getThePlayers(){
        return this.thePlayers;
    }





}
