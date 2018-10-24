package main;

import com.sun.org.apache.xpath.internal.functions.FuncFalse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Player {
    private int playerID;
    private boolean isBot;
    private boolean isJudge;
    private boolean online;
    private Socket connection;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;
    private ArrayList<String> hand;

    public Player(int playerID){
        this.playerID = playerID;
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

    private void writeToSocket(){

    }

}
