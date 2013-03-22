package com.steamedpears.comp3004.models.players;

import com.steamedpears.comp3004.models.AssetMap;
import com.steamedpears.comp3004.models.AssetSet;
import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.models.Wonder;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import static com.steamedpears.comp3004.models.Asset.*;

public class GreedyAIPlayer extends AIPlayer {

    private static Logger log = Logger.getLogger(GreedyAIPlayer.class);

    public GreedyAIPlayer(Wonder wonder, SevenWondersGame game) {
        super(wonder, game);
    }

    @Override
    protected void handleTurn(){
        Player clone = this.clone(GreedyAIPlayer.class);
        log.debug("Clone successful");
        PlayerCommand potentialCommand;
        int newTotal;

        //Default to the null command
        int bestTotal = clone.getFinalVictoryPoints()+1;
        log.debug("Starting with null move");
        setCurrentCommand(PlayerCommand.getNullCommand(this));

        //See the value of playing each card
        for(Card card: getHand()){
            if(Thread.interrupted()){
                log.debug("AI thread Interrupted");
                return;
            }
            potentialCommand = new PlayerCommand(PlayerCommand.PlayerCardAction.PLAY, card.getId());
            if(clone.isValid(potentialCommand)){
                clone.getPlayedCards().add(card);
                newTotal = getCardPlayHeuristic(clone, card);
                if(newTotal>bestTotal){
                    //best command yet, ship it! (until further notice)
                    log.debug("Playing card: "+card);
                    setCurrentCommand(potentialCommand);
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
        potentialCommand = new PlayerCommand(PlayerCommand.PlayerCardAction.BUILD, getHand().get(0).getId());
        if(clone.isValid(potentialCommand)){
            Card nextStage = clone.getWonder().getNextStage();
            clone.getWonder().buildNextStage(this);
            newTotal = getCardPlayHeuristic(clone, nextStage);
            if(newTotal>bestTotal){
                //best command yet, ship it!
                log.debug("Building wonder");
                setCurrentCommand(potentialCommand);
                bestTotal = newTotal;
            }
        }
    }

    private static int getCardPlayHeuristic(Player player, Card card) {
        AssetMap assets = player.getOptionalAssetsSummary();
        AssetMap leftAssets = player.getPlayerLeft().getAssets();
        AssetMap rightAssets = player.getPlayerRight().getAssets();
        assets.add(player.getAssets());
        AssetMap cardAssets = card.getAssets(player);
        AssetSet cardAssetsOptional = card.getAssetsOptional(player);



        int total = player.getFinalVictoryPoints();

        List resources = Arrays.asList(TRADEABLE_ASSET_TYPES);
        List sciences = Arrays.asList(ASSET_SCIENCE_1, ASSET_SCIENCE_2, ASSET_SCIENCE_3);
        for(String key: cardAssets.keySet()){
            if(resources.contains(key)){
                total+= 2*(2*cardAssets.get(key)-assets.get(key));
            }
            if(sciences.contains(key)){
                total+=3;
            }
            if(key.equals(ASSET_MILITARY_POWER)){
                int delta =  cardAssets.get(ASSET_MILITARY_POWER);
                int ownPower = assets.get(ASSET_MILITARY_POWER);
                int leftPower = leftAssets.get(ASSET_MILITARY_POWER);
                int rightPower = rightAssets.get(ASSET_MILITARY_POWER);
                if(ownPower<=leftPower && ownPower+delta>=leftPower){
                    total+=5;
                }
                if(ownPower<=rightPower && ownPower+delta>=rightPower){
                    total+=5;
                }
            }
        }

        for(String key: cardAssetsOptional){
            if(resources.contains(key)){
                total+= 2*(2*cardAssets.get(key)-assets.get(key));
            }
            if(sciences.contains(key)){
                total+=3;
            }
        }
        log.debug(card.getId()+": "+total);
        return total;
    }
}
