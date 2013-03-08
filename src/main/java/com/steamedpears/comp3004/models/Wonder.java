package com.steamedpears.comp3004.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.steamedpears.comp3004.SevenWonders;

import javax.swing.*;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Wonder {
    private class WonderSide{
        //stages are represented by cards for simplicity
        public List<Card> stages;
        public String image;
        public String startResource;

        public WonderSide(JsonObject obj){
            this.stages = Card.parseDeck(obj.getAsJsonArray(PROP_WONDER_STAGES));
            this.image = SevenWonders.PATH_IMG_WONDERS+
                    obj.getAsJsonPrimitive(PROP_WONDER_IMAGE).getAsString()
                    +".png";
            this.startResource = obj.getAsJsonPrimitive(PROP_WONDER_START_RESOURCE).getAsString();
        }
    }
    //static constants/////////////////////////////////////////////
    public static final String PROP_WONDER_SIDE_A =             "a";
    public static final String PROP_WONDER_SIDE_B =             "b";
    public static final String PROP_WONDER_NAME =               "name";
    public static final String PROP_WONDER_STAGES =             "stages";
    public static final String PROP_WONDER_IMAGE =              "image";
    public static final String PROP_WONDER_START_RESOURCE =     "startResource";

    //static methods///////////////////////////////////////////////

    /**
     * Interprets the JSON description of a list of wonders, and creates them
     * @param wonderList a JSON description of the wonders
     * @return a Map of ids to Wonders
     */
    public static Map<String,Wonder> parseWonders(JsonArray wonderList){
        Map<String, Wonder> wonders = new HashMap<String, Wonder>();
        for(JsonElement element: wonderList){
            Wonder newWonder = new Wonder(element.getAsJsonObject());
            wonders.put(newWonder.getName(),newWonder);
        }
        return wonders;
    }

    //instance variables///////////////////////////////////////////
    private String name;
    private WonderSide sideA;
    private WonderSide sideB;
    private WonderSide currentSide;
    private int currentStage = 0;

    //constructor//////////////////////////////////////////////////
    public Wonder(JsonObject obj){
        this.name = obj.getAsJsonPrimitive(PROP_WONDER_NAME).getAsString();
        this.sideA = new WonderSide(obj.getAsJsonObject(PROP_WONDER_SIDE_A));
        this.sideB = new WonderSide(obj.getAsJsonObject(PROP_WONDER_SIDE_B));
        reset();
    }

    //methods//////////////////////////////////////////////////////

    /**
     * resets the Wonder's state for a new game
     */
    public void reset(){
        currentStage = 0;
        currentSide = sideA;
    }

    /**
     * builds the next stage of the Wonder and charges the Player the stage's cost
     * @param player the Player building the Wonder's stage
     */
    public void buildNextStage(Player player){
        //TODO: build the next stage of the Wonder, and charge the Player for the cost of the stage
    }

    /**
     * informs the Wonder that its parent Player has used one of its limited resources
     * @param assetName name of resource expended
     */
    public void expendLimitedAsset(String assetName){
        //TODO: make it so the Player can't reuse this ever again, somehow (i.e. if this is Asset.ASSET_BUILD_FREE, disable it)
    }

    //setters///////////////////////////////////////////////////////

    /**
     * Set the side the Wonder uses
     * @param side the to side use
     */
    public void setSide(String side){
        if(side==PROP_WONDER_SIDE_A){
            this.currentSide = sideA;
        }else if(side==PROP_WONDER_SIDE_B){
            this.currentSide = sideB;
        }
    }

    /**
     * Randomly choose the side of the Wonder in use
     */
    public void randomizeSide(){
        if(Math.random()>0.5){
            setSide(PROP_WONDER_SIDE_A);
        }else{
            setSide(PROP_WONDER_SIDE_B);
        }
    }

    //getters///////////////////////////////////////////////////////
    public String getName(){
        return name;
    }

    public int getCurrentStage(){
        return currentStage;
    }

    public String getSide(){
        if(currentSide==sideB){
            return PROP_WONDER_SIDE_B;
        }else{
            return PROP_WONDER_SIDE_A;
        }
    }

    public URL getImagePath(){
        URL result = Card.class.getResource(currentSide.image);
        if(result==null){
            try {
                result = new URL("file://");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Card getNextStage(){
        if(currentStage<getStages().size()){
            return getStages().get(currentStage);
        }
        return null;
    }

    public List<Card> getStages(){
        return currentSide.stages;
    }

    public Map<String, Integer> getAssets(Player p){
        //TODO: get the assets this wonder definitely has

        return null;
    }

    public List<Set<String>> getOptionalAssetsComplete(Player p){
        //TODO: get a list of all the asset choices this wonder can make

        return null;
    }

    public Map<String, Integer> getTradeableAssets(){
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put(currentSide.startResource, 1);

        return result;
    }

    public String getId(){
        return this.getName()+"_"+this.getSide();
    }

    @Override
    public int hashCode(){
        return getName().hashCode();
    }
}
