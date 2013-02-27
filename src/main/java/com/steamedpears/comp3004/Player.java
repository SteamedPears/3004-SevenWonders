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

    public final void takeTurn(ArrayList<Card> cards){
        PlayerCommand command = handleTurn(cards);
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
    public final Player getPlayerLeft(){
        return playerLeft;
    }

    public final Player getPlayerRight(){
        return playerRight;
    }



}
