package com.steamedpears.comp3004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.steamedpears.comp3004.Asset.*;

public abstract class Player {
    //inner utility classes/////////////////////////////////////////////////
    protected enum PlayerCardAction{
        DISCARD, BUILD, PLAY
    }

    protected class PlayerCommand{
        public PlayerCardAction action;
        public Card card;
        public Map<String, Integer> leftPurchases;
        public Map<String, Integer> rightPurchases;
    }


    //instance variables////////////////////////////////////////////////////
    private Wonder wonder;
    private List<Card> playedCards;
    private Map<String, Integer> freeAssets;
    private Map<String, Integer> restrictedAssets;
    private Player playerLeft;
    private Player playerRight;

    //constructor///////////////////////////////////////////////////////////
    public Player(Wonder wonder){
        this.wonder = wonder;
        this.playedCards = new ArrayList<Card>();
    }

    private final void discardCard(Card card){
        //TODO: remove card from hand, get 3 gold
    }

    private final void buildWonder(Card card){
        //TODO: buildup wonder, return whether successful
    }

    private final void playCard(Card card){
        //TODO: play card, return whether successful
    }

    public final void takeTurn(ArrayList<Card> currentHand) throws Exception {
        PlayerCommand command = handleTurn(currentHand);
        if(!isValid(command, currentHand)){
            throw new Exception("Player made invalid move");
        }
        if(command!=null){
            if(command.action.equals(PlayerCardAction.BUILD)){
                buildWonder(command.card);
            }else if(command.action.equals(PlayerCardAction.PLAY)){
                playCard(command.card);
            }else if(command.action.equals(PlayerCardAction.DISCARD)){
                discardCard(command.card);
            }
        }
    }

    //extend with a GUI or AI
    protected abstract PlayerCommand handleTurn(ArrayList<Card> cardsInHand);

    //setters///////////////////////////////////////////////////////////////////
    public final void setPlayerLeft(Player playerLeft){
        this.playerLeft = playerLeft;
    }

    public final void setPlayerRight(Player playerRight){
        this.playerRight = playerRight;
    }

    //getters///////////////////////////////////////////////////////////////////
    private final boolean isValid(PlayerCommand command, List<Card> cards){
        //TODO: determine if the given command is a valid one for the player to perform

        return false;
    }

    public final Player getPlayerLeft(){
        return playerLeft;
    }

    public final Player getPlayerRight(){
        return playerRight;
    }

    public final Map<String, Integer> getAssets(){
        //TODO: get the assets a player definitely has

        return null;
    }

    public final List<Map<String, Integer>> getOptionalAssetsComplete(){
        //TODO: get a list of all the asset choices a player can make

        return null;
    }

    public final Map<String, Integer> getOptionalAssetsSummary(){
        //TODO: get a map representing the total of all the asset choices a player can make
        //e.g. wood/stone and stone/clay -> wood:1, stone:2, clay:1
        return null;
    }

    public final int getCountOfAsset(String assetName){
        return getAsset(getAssets(), assetName);
    }
}
