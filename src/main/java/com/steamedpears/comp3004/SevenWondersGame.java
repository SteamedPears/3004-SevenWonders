package com.steamedpears.comp3004;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SevenWondersGame extends Thread{
    private List<Player> players;
    private Set<Player> localPlayers;
    private Set<Card> discard;
    private List<List<Card>> deck;
    private int age;
    private int maxPlayers; //useless?
    private Router router;

    public SevenWondersGame(Router router){
        players = new ArrayList<Player>();
        localPlayers = new HashSet<Player>();
        discard = new HashSet<Card>();
        age = 1;
        this.router = router;
    }

    public void handleMoves(Map<Player, PlayerCommand> commands){
        //TODO: locally apply these moves sent from the Router
        //note: all player moves must be verified before any player move is actually applied
        //at the end just call takeTurn()? Maybe?
    }

    private void takeTurn(){
        changeHands();
        Set<Player> runningPlayers = new HashSet<Player>();

        for(Player player: localPlayers){
            player.start();
            runningPlayers.add(player);
        }

        while(runningPlayers.size()>0){
            //TODO: timeout slow players
            for(Player player: runningPlayers){
                if(!player.isAlive()){
                    router.registerMove(player, player.getCurrentCommand());
                }
            }
            try{
                Thread.sleep(100);
            }catch(InterruptedException ex){
                //TODO: find out what should be done here
            }
        }
    }

    public void run(){
        takeTurn();
    }

    private void changeHands(){
        //TODO: either deal new hands, or rotate the hands
    }

    public void discard(Card card){
        //TODO: discard this card
    }

    public void undiscard(Card card){
        //TODO: undiscard this card
    }

    public void setDeck(JsonObject obj){
        //TODO: compute the deck
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void addLocalPlayer(Player player){
        localPlayers.add(player);
        addPlayer(player);
    }

    public List<Player> getPlayers(){
        return players;
    }

    public Set<Card> getDiscard(){
        return discard;
    }

    public int getAge(){
        return age;
    }
}
