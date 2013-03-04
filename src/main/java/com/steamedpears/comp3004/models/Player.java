package com.steamedpears.comp3004.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.steamedpears.comp3004.models.Asset.*;

public abstract class Player extends Thread{
    //instance variables////////////////////////////////////////////////////
    private Wonder wonder;
    private List<Card> playedCards;
    private Map<String, Integer> freeAssets;
    private Map<String, Integer> restrictedAssets;
    private Player playerLeft;
    private Player playerRight;
    private SevenWondersGame game;
    private PlayerCommand currentCommand;
    private List<Card> hand;

    //constructor///////////////////////////////////////////////////////////
    public Player(Wonder wonder, SevenWondersGame game){
        this.wonder = wonder;
        this.game = game;
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

    private final void undiscard(Card card){
        //TODO: take this card out of this discard, and play it
    }

    private final void playFree(Card card){
        //TODO: take this card out of this discard, and play it
    }

    public final void takeTurn(PlayerCommand command) throws Exception {
        if(command!=null){
            if(command.action.equals(PlayerCommand.PlayerCardAction.BUILD)){
                buildWonder(command.card);
            }else if(command.action.equals(PlayerCommand.PlayerCardAction.PLAY)){
                playCard(command.card);
            }else if(command.action.equals(PlayerCommand.PlayerCardAction.DISCARD)){
                discardCard(command.card);
            }else if(command.action.equals(PlayerCommand.PlayerCardAction.UNDISCARD)){
                undiscard(command.card);
            }else if(command.action.equals(PlayerCommand.PlayerCardAction.PLAY_FREE)){
                playFree(command.card);
            }
        }
    }

    //extend with a GUI or AI
    //when handleTurn terminates, currentCommand should be set with the Player's desired command
    protected abstract PlayerCommand handleTurn();

    public void run(){
        handleTurn();
    }

    //setters///////////////////////////////////////////////////////////////////
    public final void setPlayerLeft(Player playerLeft){
        this.playerLeft = playerLeft;
    }

    public final void setPlayerRight(Player playerRight){
        this.playerRight = playerRight;
    }

    protected final void setCurrentCommand(PlayerCommand currentCommand){
        this.currentCommand = currentCommand;
    }

    public final void setHand(List<Card> hand){
        this.hand = hand;
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

    public final List<Card> getHand(){
        return hand;
    }

    public final PlayerCommand getCurrentCommand(){
        return currentCommand;
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

    public final Map<String, Integer> getAssetsTradeable(){
        //TODO: get the tradeable assets a player definitely has

        return null;
    }

    public final List<Map<String, Integer>> getOptionalAssetsCompleteTradeable(){
        //TODO: get a list of all the tradeable asset choices a player can make

        return null;
    }

    public final int getCountOfAsset(String assetName){
        return getAsset(getAssets(), assetName);
    }
}
