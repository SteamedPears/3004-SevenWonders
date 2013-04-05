package com.steamedpears.comp3004.models.players.strategies;

import com.steamedpears.comp3004.models.AssetMap;
import com.steamedpears.comp3004.models.AssetSet;
import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.models.Wonder;
import com.steamedpears.comp3004.models.players.AIPlayer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.steamedpears.comp3004.models.Asset.*;

public class GreedyStrategy implements Strategy {

    private static Logger log = Logger.getLogger(GreedyStrategy.class);

    @Override
    public void handleTurn(Player player){
        Player clone = player.clone(new GreedyStrategy());
        log.debug("Clone successful");
        PlayerCommand potentialCommand;
        int newTotal;

        //Default to the null command
        int bestTotal = clone.getFinalVictoryPoints()+1;
        log.debug("Starting with null move");
        player.setCurrentCommand(PlayerCommand.getNullCommand(player));

        AssetMap cloneAssets = clone.getAssets();

        //See the value of playing each card
        for(Card card: player.getHand()){
            if(Thread.interrupted()){
                log.debug("AI thread Interrupted");
                return;
            }
            potentialCommand = new PlayerCommand(PlayerCommand.PlayerCardAction.PLAY, card.getId());
            setValidTradesFor(clone, potentialCommand, cloneAssets);
            if(clone.isValid(potentialCommand)){
                clone.getPlayedCards().add(card);
                newTotal = getCardPlayHeuristic(clone, potentialCommand);
                if(newTotal>bestTotal){
                    //best command yet, ship it! (until further notice)
                    log.debug("Playing card: "+card);
                    player.setCurrentCommand(potentialCommand);
                    bestTotal = newTotal;
                }
                clone.getPlayedCards().remove(card);
            }
        }

        if(Thread.interrupted()){
            log.debug("AI thread Interrupted");
            return;
        }
        //See the value of building the wonder
        potentialCommand = new PlayerCommand(PlayerCommand.PlayerCardAction.BUILD, player.getHand().get(0).getId());
        setValidTradesFor(clone, potentialCommand, cloneAssets);
        if(clone.isValid(potentialCommand)){
            Card nextStage = clone.getWonder().getNextStage();
            clone.getWonder().buildNextStage(player);
            newTotal = getCardPlayHeuristic(clone, potentialCommand);
            if(newTotal>bestTotal){
                //best command yet, ship it!
                log.debug("Building wonder");
                player.setCurrentCommand(potentialCommand);
                bestTotal = newTotal;
            }
        }
    }

    private static int getCardPlayHeuristic(Player player, PlayerCommand command) {
        Card card = player.getGame().getCardById(command.cardID);
        AssetMap assets = player.getOptionalAssetsSummary();
        AssetMap leftAssets = player.getPlayerLeft().getAssets();
        AssetMap rightAssets = player.getPlayerRight().getAssets();
        assets.add(player.getAssets());
        AssetMap cardAssets = card.getAssets(player);
        AssetSet cardAssetsOptional = card.getAssetsOptional(player);

        int tradeCost = player.getCostOfTrade(command.leftPurchases,player.getDiscounts(player.getPlayerLeft()))
                + player.getCostOfTrade(command.rightPurchases,player.getDiscounts(player.getPlayerRight()));
        player.changeGold(-tradeCost);
        int base = player.getFinalVictoryPoints();
        player.changeGold(tradeCost);
        int heuristic = 0;

        List resources = Arrays.asList(ALL_ASSET_TYPES);
        List sciences = Arrays.asList(ASSET_SCIENCE_1, ASSET_SCIENCE_2, ASSET_SCIENCE_3);

        for(String key: cardAssets.keySet()){
            if(resources.contains(key)){
                heuristic+= 3*(2*cardAssets.get(key)-assets.get(key));
            }
            if(sciences.contains(key)){
                heuristic+=3;
            }
            if(key.equals(ASSET_MILITARY_POWER)){
                int delta =  cardAssets.get(ASSET_MILITARY_POWER);
                int ownPower = assets.get(ASSET_MILITARY_POWER)-delta;
                int leftPower = leftAssets.get(ASSET_MILITARY_POWER);
                int rightPower = rightAssets.get(ASSET_MILITARY_POWER);
                if(ownPower<=leftPower && ownPower+delta>=leftPower){
                    heuristic+=3*(5-player.getGame().getAge());
                }
                if(ownPower<=rightPower && ownPower+delta>=rightPower){
                    heuristic+=3*(5-player.getGame().getAge());
                }
            }
        }

        for(String key: cardAssetsOptional){
            if(resources.contains(key)){
                heuristic+= 3*(2*cardAssets.get(key)-assets.get(key));
            }
            if(sciences.contains(key)){
                heuristic+=3;
            }
        }
        log.debug(player+" - base: "+base+"; heur: "+heuristic+"; card: "+card.getId());
        return base+heuristic;
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
