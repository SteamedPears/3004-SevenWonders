package com.steamedpears.comp3004.models.players.strategies;

import com.steamedpears.comp3004.models.*;
import com.steamedpears.comp3004.models.assets.AssetMap;
import com.steamedpears.comp3004.models.assets.AssetSet;
import com.steamedpears.comp3004.models.players.Player;
import com.steamedpears.comp3004.models.players.strategies.heuristics.Heuristic;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class HeuristicStrategy implements Strategy {

    private static Logger log = Logger.getLogger(HeuristicStrategy.class);

    public List<Heuristic> heuristics;

    public HeuristicStrategy(){
        this.heuristics = new ArrayList<Heuristic>();
        this.heuristics.addAll(Heuristic.getHeuristics());
    }

    public HeuristicStrategy(Set<Integer> heuristicIndices){
        this.heuristics = new ArrayList<Heuristic>();
        List<Heuristic> registeredHeuristics = Heuristic.getHeuristics();
        for(Integer index: heuristicIndices){
            this.heuristics.add(registeredHeuristics.get(index));
        }
    }

    @Override
    public void handleTurn(Player player){
        Player clone = player.clone(new HeuristicStrategy());
        log.debug("Clone successful");
        PlayerCommand potentialCommand;
        int newTotal;

        //Default to the null command
        int bestTotal = 0;
        log.debug("Starting with null move");
        player.setCurrentCommand(PlayerCommand.getNullCommand(player));

        AssetMap cloneAssets = clone.getAssets();

        //See the value of playing each card
        for(Card card: clone.getHand()){
            if(Thread.interrupted()){
                log.debug("AI thread Interrupted");
                return;
            }
            potentialCommand = new PlayerCommand(PlayerCommand.PlayerCardAction.PLAY, card.getId());
            setValidTradesFor(clone, potentialCommand, cloneAssets);
            if(clone.isValid(potentialCommand)){
                newTotal = getPlayHeuristic(clone, potentialCommand);
                if(newTotal>bestTotal){
                    //best command yet, ship it! (until further notice)
                    log.debug("Playing card: "+card);
                    player.setCurrentCommand(potentialCommand);
                    bestTotal = newTotal;
                }
            }
            if(Thread.interrupted()){
                log.debug("AI thread Interrupted");
                return;
            }
            potentialCommand = new PlayerCommand(PlayerCommand.PlayerCardAction.DISCARD, card.getId());
            newTotal = getPlayHeuristic(clone, potentialCommand);
            if(newTotal>bestTotal){
                //best command yet, ship it! (until further notice)
                log.debug("Discarding card: "+card);
                player.setCurrentCommand(potentialCommand);
                bestTotal = newTotal;
            }
        }

        if(Thread.interrupted()){
            log.debug("AI thread Interrupted");
            return;
        }
        //See the value of building the wonder
        potentialCommand = new PlayerCommand(PlayerCommand.PlayerCardAction.BUILD, clone.getHand().get(0).getId());
        setValidTradesFor(clone, potentialCommand, cloneAssets);
        if(clone.isValid(potentialCommand)){
            newTotal = getPlayHeuristic(clone, potentialCommand);
            if(newTotal>bestTotal){
                //best command yet, ship it!
                log.debug("Building wonder");
                player.setCurrentCommand(potentialCommand);
                bestTotal = newTotal;
            }
        }
    }

    private int getPlayHeuristic(Player player, PlayerCommand command) {
        int totalHeuristic = 0;
        for(Heuristic heuristic: heuristics){
            totalHeuristic+= heuristic.getHeuristic(player, command);
        }
        return totalHeuristic;
    }

    private static void setValidTradesFor(Player player, PlayerCommand command, AssetMap playerAssets){
        Card card = player.getGame().getCardById(command.cardID);
        if(card.canAfford(player, command)){
            return;
        }

        AssetMap costLessBase = new AssetMap();

        costLessBase.add(card.getCost(player));
        costLessBase.subtract(playerAssets);

        List<AssetMap> choices = new ArrayList<AssetMap>();
        List<List<AssetSet>> optionalChoices = new ArrayList<List<AssetSet>>();
        List<AssetSet> discounts = new ArrayList<AssetSet>();
        List<Player> targets = Arrays.asList(player.getPlayerLeft(), player.getPlayerRight());

        for(Player target: targets){
            choices.add(target.getAssetsTradeable());
            optionalChoices.add(target.getOptionalAssetsCompleteTradeable());
            discounts.add(player.getDiscounts(target));
        }

        //please work...
        log.debug(player+" - Looking for valid trades for "+card.getId());
        List<AssetMap> result = costLessBase.getValidChoices(choices, optionalChoices, discounts);
        if(result!=null){
            command.leftPurchases = result.get(0);
            command.rightPurchases = result.get(1);
            log.debug(player+" - Got positive result for "+command+": "+command.leftPurchases+" "+command.rightPurchases);
        }else{
            log.debug(player+" - Got negative result for "+command);
        }
    }


}
