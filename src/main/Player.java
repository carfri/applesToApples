package main;

import java.util.ArrayList;

public class Player {
    private boolean isBot;
    private boolean isJudge;
    public int id;
    private int choice;
    private int judgeChoice;
    private ArrayList<String> hand;
    private int score;
    private String playedCard;

    public Player(boolean isBot, int id){
        this.isBot = isBot;
        this.isJudge = false;
        this.id = id;
    }

    public boolean getIsBot(){
        return this.isBot;
    }

    public void setHand(ArrayList<String> hand) {
        this.hand = hand;
    }

    public ArrayList<String> getHand(){
        return this.hand;
    }

    public void setJudge(boolean flag){
        this.isJudge = flag;
    }

    public boolean getJudge(){
        return this.isJudge;
    }

    public void setChoice(int choice){
        this.choice = choice;
    }

    public int getChoice(){
        return this.choice;
    }

    public void setJudgeChoice(int judgeChoice){
        this.judgeChoice = judgeChoice;
    }

    public int getJudgeChoice(){
        return this.judgeChoice;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void playCard(int card){
        playedCard = hand.get(card);
        hand.remove(card);
    }

    public void drawCard(String card){
        hand.add(card);
    }

    public String getPlayedCard(){
        return playedCard;
    }

}
