package com.steamedpears.comp3004;

import com.google.gson.JsonObject;

import java.awt.Image;
import java.util.List;
import java.util.Map;

public class Card {
    //constants/////////////////////////////////////////////////////
    public static final String PLAYER_LEFT = "left";
    public static final String PLAYER_RIGHT = "right";
    public static final String PLAYER_SELF = "self";

    //instance variables////////////////////////////////////////////
    private String color;
    private String name;
    private Image image;
    //The base amount of assets this card yields
    private Map<String, Integer> baseAssets;
    //If multiplierAssets is not null, multiply the values in baseAssets
    //by the number of occurences of these assets in multiplierTargets (a combination of self, left, and right)
    private List<String> multiplierAssets;
    private List<String> multiplierTargets;
    //The building the card is free to play for
    private String freeFor;
    //the cost of playing this card
    private Map<String, Integer> cost;
    //the resources this card makes discounted
    private List<String> discountsAssets;
    //the players to which this card's discountsAssets applies to
    private List<String> discountsTargets;
    //if true, the player may choose only *one* of the keys of baseAssets
    private boolean isChoice;

    //constructor///////////////////////////////////////////////////
    public Card(JsonObject obj){
        //TODO: interpret this object
    }

    //getters////////////////////////////////////////////////////////
    public String getName(){
        return name;
    }

    public String getColor(){
        return color;
    }

    public Image getImage(){
        return image;
    }

    public Map<String, Integer> getAssets(Player player){
        //TODO: compute the assets this card yields if played by this player

        return null;
    }

    public List<String> getAssetsOptional(Player player){
        //TODO: compute the list of assets this card yields if it isChoice

        return null;
    }
}
