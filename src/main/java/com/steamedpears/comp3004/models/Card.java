package com.steamedpears.comp3004.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.steamedpears.comp3004.SevenWonders;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.steamedpears.comp3004.models.Asset.*;

public class Card {
    //constants/////////////////////////////////////////////////////
    public static final String PLAYER_LEFT =    "left";
    public static final String PLAYER_RIGHT =   "right";
    public static final String PLAYER_SELF =    "self";

    public static final String PROP_CARD_NAME =                 "name";
    public static final String PROP_CARD_COLOR =                "guild";
    public static final String PROP_CARD_COST =                 "cost";
    public static final String PROP_CARD_MIN_PLAYERS =          "players";
    public static final String PROP_CARD_AGE =                  "age";
    public static final String PROP_CARD_BASE_ASSETS =          "baseAssets";
    public static final String PROP_CARD_MULTIPLIER_ASSETS =    "multiplierAssets";
    public static final String PROP_CARD_MULTIPLIER_TARGETS =   "multiplierTargets";
    public static final String PROP_CARD_DISCOUNTS_ASSETS =     "discountsAssets";
    public static final String PROP_CARD_DISCOUNTS_TARGETS =    "discountsTargets";
    public static final String PROP_CARD_CHOICE =               "isChoice";
    public static final String PROP_CARD_FREE_FOR =             "freeFor";

    private static Logger log = Logger.getLogger(Card.class);

    //static methods////////////////////////////////////////////////

    /**
     * Parses the given JSON array describing a list of cards into a Java List of Cards in the same order
     * @param deck JSON array of cards
     * @return the List of Cards
     */
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
    private String image;
    private URL imageURL;
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
    private String id;

    //constructor///////////////////////////////////////////////////
    public Card(JsonObject obj){
        this.name = obj.has(PROP_CARD_NAME) ? obj.getAsJsonPrimitive(PROP_CARD_NAME).getAsString() : "";
        this.color = obj.has(PROP_CARD_COLOR) ? obj.getAsJsonPrimitive(PROP_CARD_COLOR).getAsString() : "";
        this.cost = convertJSONToAssetMap(obj, PROP_CARD_COST);
        this.minPlayers = obj.has(PROP_CARD_MIN_PLAYERS) ? obj.getAsJsonPrimitive(PROP_CARD_MIN_PLAYERS).getAsInt() : 0;
        this.age = obj.has(PROP_CARD_AGE) ? obj.getAsJsonPrimitive(PROP_CARD_AGE).getAsInt() : 0;
        this.id = this.getName().replace(" ","")+"_"+this.age+"_"+this.minPlayers;
        this.image = SevenWonders.PATH_IMG_CARDS+getId()+SevenWonders.IMAGE_TYPE_SUFFIX;

        //figure out what this card actually does
        this.baseAssets = convertJSONToAssetMap(obj, PROP_CARD_BASE_ASSETS);
        this.multiplierAssets = convertJSArrayToSet(obj,PROP_CARD_MULTIPLIER_ASSETS);
        this.multiplierTargets = convertJSArrayToSet(obj,PROP_CARD_MULTIPLIER_TARGETS);
        this.discountsAssets = convertJSArrayToSet(obj,PROP_CARD_DISCOUNTS_ASSETS);
        this.discountsTargets = convertJSArrayToSet(obj,PROP_CARD_DISCOUNTS_TARGETS);
        this.isChoice = obj.has(PROP_CARD_CHOICE) && obj.getAsJsonPrimitive(PROP_CARD_CHOICE).getAsBoolean();
        this.freeFor = obj.has(PROP_CARD_FREE_FOR) ? obj.getAsJsonPrimitive(PROP_CARD_FREE_FOR).getAsString() : "";
    }

    //getters////////////////////////////////////////////////////////

    /**
     * Gets the name of the Card
     * @return the name of the Card
     */
    public String getName(){
        return name;
    }

    /**
     * Gets the colour of the Card
     * @return the colour of the Card
     */
    public String getColor(){
        return color;
    }

    /**
     * Gets the URL of the Card's image
     * @return the URL of the Card's image
     */
    public URL getImagePath(){
        if(imageURL==null){
            URL result = Card.class.getResource(image);
            if(result==null){
               log.warn("Missing file " + image);
                try {
                    result = new URL("file://");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            imageURL = result;
        }
        return imageURL;
    }

    /**
     * Gets the minimum number of Players necessary to use this card in a game
     * @return the minimum number of Players necessary to use this card in a game
     */
    public int getMinPlayers(){
        return minPlayers;
    }

    /**
     * Gets the age this Card is to be part of
     * @return the age this Card is to be part of
     */
    public int getAge(){
        return age;
    }

    /**
     * Gets the name of the building that, if already constructed, makes this Card free to play
     * @return the name of the building that, if already constructed, makes this Card free to play
     */
    public String getFreeFor(){
        return freeFor;
    }

    /**
     * Gets the Asset cost of playing this card
     * @return the Asset cost of playing this card
     */
    public Map<String, Integer> getCost(){
        return cost;
    }

    /**
     * Gets the Assets that this Card will make cheaper to buy from neighbours
     * @return the Assets that this Card will make cheaper to buy from neighbours
     */
    public Set<String> getDiscountsAssets(){
        return discountsAssets;
    }

    /**
     * Gets the neighbours for which #getDiscountsAssets applies to
     * @return the neighbours for which #getDiscountsAssets applies to
     */
    public Set<String> getDiscountsTargets(){
        return discountsTargets;
    }

    /**
     * Charges the given Player the cost of playing this card
     * @param player the player being charged
     */
    public void playCard(Player player){
        player.changeGold(-Asset.getAsset(getCost(),Asset.ASSET_GOLD));
    }

    /**
     * Gets the Assets this Card would yield the given Player, if played, before optional choices
     * @param player the player that would play this card
     * @return the Assets
     */
    public Map<String, Integer> getAssets(Player player){
        Map<String, Integer> result = new HashMap<String, Integer>();
        if(!isChoice){
            int multiplier = 1;
            if(!multiplierAssets.isEmpty()){
                multiplier = 0;
                for(String target: multiplierTargets){
                    Player targetPlayer;
                    if(target.equals(PLAYER_LEFT)){
                        targetPlayer = player.getPlayerLeft();
                    }else if(target.equals(PLAYER_RIGHT)){
                        targetPlayer = player.getPlayerRight();
                    }else{ //self
                        targetPlayer = player;
                    }
                    Map<String, Integer> targetPlayerAssets = targetPlayer.getConcreteAssets();
                    for(String multiplierAsset: multiplierAssets){
                        multiplier+=Asset.getAsset(targetPlayerAssets, multiplierAsset);
                    }
                }
            }
            for(String asset: baseAssets.keySet()){
                result.put(asset, baseAssets.get(asset)*multiplier);
            }
        }
        return result;
    }

    /**
     * Gets the optional Assets this Card would yield the given Player, if played
     * @param player the player that would play this card
     * @return the Assets
     */
    public Set<String> getAssetsOptional(Player player){
        Set<String> result = new HashSet<String>();
        if(isChoice){
            for(String asset: baseAssets.keySet()){
                result.add(asset);
            }
        }
        return result;
    }

    /**
     * Gets the unique ID for this Card
     * @return a unique ID
     */
    public String getId(){
        return this.id;
    }

    @Override
    public int hashCode(){
        return getId().hashCode();
    }

    /**
     * Determines whether the given Player can afford this Card, if they perform the purchases stated in the command
     * @param player the Player that would play the Card
     * @param command the command that would play the Card
     * @return whether the Player can afford this Card
     */
    public boolean canAfford(Player player, PlayerCommand command) {
        Map<String, Integer> costLessBase = new HashMap<String, Integer>();
        Map<String, Integer> playerAssets = player.getAssets();
        for(String asset: cost.keySet()){
            int assetQuantity = cost.get(asset);
            assetQuantity-=getAsset(playerAssets, asset);
            assetQuantity-=getAsset(command.leftPurchases, asset);
            assetQuantity-=getAsset(command.rightPurchases, asset);
            if(assetQuantity>0){
                costLessBase.put(asset, assetQuantity);
            }
        }
        if(costLessBase.size()==0){
            return true;
        }else{
            return existsValidChoices(player.getOptionalAssetsComplete(), costLessBase);
        }
    }


}
