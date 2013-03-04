package com.steamedpears.comp3004.models;

import com.google.gson.JsonObject;

import java.awt.Image;
import java.util.List;
import java.util.Map;

public class Wonder {
    private class WonderSide{
        //stages are represented by cards for simplicity
        public List<Card> stages;
        public Image image;
        public String startResource;
    }
    //static constants/////////////////////////////////////////////
    public static final char SIDE_A = 'A';
    public static final char SIDE_B = 'B';

    //instance variables///////////////////////////////////////////
    private String name;
    private WonderSide sideA;
    private WonderSide sideB;
    private WonderSide currentSide;
    private int currentStage = 0;

    //constructor//////////////////////////////////////////////////
    public Wonder(JsonObject obj){
        //TODO: interpret this object

        reset();
    }

    //methods//////////////////////////////////////////////////////
    public void reset(){
        currentStage = 0;
        currentSide = sideA;
    }

    public boolean buildNextStage(Player player, Card card){
        //TODO: remove card from player's hand
        return false;
    }

    //setters///////////////////////////////////////////////////////
    public void setSide(char side){
        if(side==SIDE_A){
            this.currentSide = sideA;
        }else if(side==SIDE_B){
            this.currentSide = sideB;
        }
    }

    //getters///////////////////////////////////////////////////////
    public String getName(){
        return name;
    }

    public int getCurrentStage(){
        return currentStage;
    }

    public char getSide(){
        if(currentSide==sideB){
            return SIDE_B;
        }else{
            return SIDE_A;
        }
    }

    public Map<String, Integer> getCostOfNextStage(){
        //TODO: return cost of next stage

        return null;
    }
}
