package com.steamedpears.comp3004;

import com.google.gson.JsonObject;

import java.awt.Image;
import java.util.List;
import java.util.Map;

public class Wonder {
    //static constants/////////////////////////////////////////////
    public static final char SIDE_A = 'A';
    public static final char SIDE_B = 'B';

    //instance variables///////////////////////////////////////////
    private String name;
    private char side;
    //stages are represented by cards for simplicity
    private List<Card> stagesA;
    private List<Card> stagesB;
    private int currentStage;
    private Image image;

    //constructor//////////////////////////////////////////////////
    public Wonder(JsonObject obj){
        //TODO: interpret this object

        reset();
    }

    //methods//////////////////////////////////////////////////////
    public void reset(){
        currentStage = 0;
    }

    public boolean buildNextStage(Player player, Card card){
        //TODO: remove card from player's hand
        return false;
    }

    //setters///////////////////////////////////////////////////////
    public void setSide(char side){
        this.side = side;
    }

    //getters///////////////////////////////////////////////////////
    public String getName(){
        return name;
    }

    public int getCurrentStage(){
        return currentStage;
    }

    public char getSide(){
        return side;
    }

    public Map<String, Integer> getCostOfNextStage(){
        //TODO: return cost of next stage

        return null;
    }
}
