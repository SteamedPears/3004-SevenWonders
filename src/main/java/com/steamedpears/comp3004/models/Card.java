package com.steamedpears.comp3004.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.steamedpears.comp3004.models.Asset.*;

public class Card {
    //constants/////////////////////////////////////////////////////
    public static final String PLAYER_LEFT = "left";
    public static final String PLAYER_RIGHT = "right";
    public static final String PLAYER_SELF = "self";

    //static methods////////////////////////////////////////////////
    public static List<Card> parseDeck(JsonArray deck){
        List<Card> cards = new ArrayList<Card>();
        for(JsonElement element: deck){
            cards.add(new Card(element.getAsJsonObject()));
        }
        return cards;
    }

    //instance variables////////////////////////////////////////////
    private String color;
    private String name;
    private Image image;
    private int minPlayers;
    private int age;
    //The base amount of assets this card yields
    private Map<String, Integer> baseAssets;
    //If multiplierAssets is not null, multiply the values in baseAssets
    //by the number of occurences of these assets in multiplierTargets (a combination of self, left, and right)
    private Set<String> multiplierAssets;
    private Set<String> multiplierTargets;
    //The building the card is free to play for
    private String freeFor;
    //the cost of playing this card
    private Map<String, Integer> cost;
    //the resources this card makes discounted
    private Set<String> discountsAssets;
    //the players to which this card's discountsAssets applies to
    private Set<String> discountsTargets;
    //if true, the player may choose only *one* of the keys of baseAssets
    private boolean isChoice;

    //constructor///////////////////////////////////////////////////
    public Card(JsonObject obj){
        this.name = obj.has("name") ? obj.getAsJsonPrimitive("name").getAsString() : "";
        this.color = obj.has("color") ? obj.getAsJsonPrimitive("guild").getAsString() : "";
        this.image = obj.has("image") ? new ImageIcon(obj.getAsJsonPrimitive("image").getAsString()).getImage() : null;
        this.cost = convertJSONToAssetMap(obj, "cost");
        this.minPlayers = obj.has("players") ? obj.getAsJsonPrimitive("players").getAsInt() : 0;
        this.age = obj.has("age") ? obj.getAsJsonPrimitive("age").getAsInt() : 0;

        //figure out what this card actually does
        this.baseAssets = convertJSONToAssetMap(obj,"baseAssets");
        this.multiplierAssets = convertJSArrayToSet(obj,"multiplierAssets");
        this.multiplierTargets = convertJSArrayToSet(obj,"multiplierTargets");
        this.discountsAssets = convertJSArrayToSet(obj,"discountsAssets");
        this.discountsTargets = convertJSArrayToSet(obj,"discountsTargets");
        this.isChoice = obj.has("isChoice") && obj.getAsJsonPrimitive("isChoice").getAsBoolean();
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

    public int getMinPlayers(){
        return minPlayers;
    }

    public int getAge(){
        return age;
    }

    public Map<String, Integer> getAssets(Player player){
        //TODO: compute the assets this card yields if played by this player

        return null;
    }

    public Set<String> getAssetsOptional(Player player){
        //TODO: compute the list of assets this card yields if it isChoice

        return null;
    }
}
