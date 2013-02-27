package com.steamedpears.comp3004;

import com.google.gson.JsonObject;

import java.awt.Image;
import java.util.Map;

public class Card {
    private String color;
    private String name;
    private Image image;
    private Map<String, Integer> baseAssets;

    public Card(JsonObject obj){
        //TODO: interpret this object
    }

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
}
