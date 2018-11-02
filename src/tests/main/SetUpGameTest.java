package main;

import jdk.nashorn.internal.parser.Token;
import org.junit.Before;
import org.junit.Test;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import static org.junit.Assert.*;

public class SetUpGameTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetGreenApples() {
        SetUpGame setUpGame = new SetUpGame(2,2);
        ArrayList<String> actual = new ArrayList<String>();
        actual = setUpGame.getGreenApples();
        ArrayList<String> expected = new ArrayList<String>();
        try{
            File redFile = new File("greenApples.txt");
            Scanner reader = new Scanner(redFile);
            while (reader.hasNextLine())
                expected.add(reader.nextLine());
        }catch (FileNotFoundException exception){
            System.out.println("Cannot find greenApples.txt");
        }

        assertEquals(expected.size(),actual.size());
    }

    @Test
    public void testGetRedApples() {
        SetUpGame setUpGame = new SetUpGame(2,2);
        ArrayList<String> actual = new ArrayList<String>();
        actual = setUpGame.getRedApples();
        ArrayList<String> expected = new ArrayList<String>();
        try{
            File redFile = new File("redApples.txt");
            Scanner reader = new Scanner(redFile);
            while (reader.hasNextLine())
                expected.add(reader.nextLine());
        }catch (FileNotFoundException exception){
            System.out.println("Cannot find redApples.txt");
        }

        assertEquals(expected.size(),actual.size());
    }

    @Test
    public void testShuffleDeck(){
        SetUpGame setUpGame = new SetUpGame(2,2);
        ArrayList<String> expected = new ArrayList<String>();
        try{
            File redFile = new File("redApples.txt");
            Scanner reader = new Scanner(redFile);
            while (reader.hasNextLine()){
                expected.add(reader.nextLine());
            }
        }catch (FileNotFoundException exception){
            System.out.println("Cannot find redApples.txt");
        }
        assertFalse(Arrays.equals(expected.toArray(), setUpGame.shuffleDeck(expected).toArray()));
    }
}