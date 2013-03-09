package com.steamedpears.comp3004.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.steamedpears.comp3004.SevenWonders;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Wonder {
    private class WonderSide{
        //stages are represented by cards for simplicity
        public List<Card> stages;
        public String image;
        public String startResource;
        public URL imageURL;

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
        if(side.equals(PROP_WONDER_SIDE_A)){
            this.currentSide = sideA;
        }else if(side.equals(PROP_WONDER_SIDE_B)){
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

    /**
     * Gets the name of the Wonder
     * @return the name of the Wonder
     */
    public String getName(){
        return name;
    }

    /**
     * Gets the current stage of the Wonder
     * @return the current stage of the Wonder
     */
    public int getCurrentStage(){
        return currentStage;
    }

    /**
     * Gets the current side of the Wonder
     * @return the current side of the Wonder
     */
    public String getSide(){
        if(currentSide==sideB){
            return PROP_WONDER_SIDE_B;
        }else{
            return PROP_WONDER_SIDE_A;
        }
    }

    /**
     * Get the URL to the image representation of this Card
     * @return the URL of this card's image
     */
    public URL getImagePath(){
        if(currentSide.imageURL==null){
            URL result = Card.class.getResource(currentSide.image);
            if(result==null){
                try {
                    result = new URL("file://");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            currentSide.imageURL = result;
        }
        return currentSide.imageURL;
    }

    /**
     * Get the Card representing the next stage of this Wonder
     * @return the next stage of the Wonder, or null if none exists
     */
    public Card getNextStage(){
        if(currentStage<getStages().size()){
            return getStages().get(currentStage);
        }
        return null;
    }

    /**
     * Get a List of the Card representations of this Wonder's stages
     * @return the stages of the Wonder
     */
    public List<Card> getStages(){
        return currentSide.stages;
    }

    /**
     * Gets the Assets this Wonder currently yields the given Player, without making ny optional choices
     * @param p the Player that owns the Wonder
     * @return the Assets this Wonder definitely yields
     */
    public Map<String, Integer> getAssets(Player p){
        //TODO: get the assets this wonder definitely has

        return new HashMap<String, Integer>();
    }

    /**
     * Gets a List of the choices of Assets this Wonder currently yields for the given Player
     * @param p the Player that owns the Wonder
     * @return a List of choices of Assets this Wonder currently yields
     */
    public List<Set<String>> getOptionalAssetsComplete(Player p){
        //TODO: get a list of all the asset choices this wonder can make

        return new ArrayList<Set<String>>();
    }

    /**
     * Gets all the tradeable Assets this Wonder yields (just the start resource)
     * @return the tradeable Assets this Wonder yields
     */
    public Map<String, Integer> getTradeableAssets(){
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put(currentSide.startResource, 1);

        return result;
    }

    /**
     * Gets the unique id of the Wonder (factoring in also the side it is on)
     * @return a unique id
     */
    public String getId(){
        return this.getName()+"_"+this.getSide();
    }

    @Override
    public int hashCode(){
        return getName().hashCode();
    }
}
