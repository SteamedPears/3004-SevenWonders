package com.steamedpears.comp3004.models.players;

import com.steamedpears.comp3004.models.Asset;
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
        Player clone = this.clone();
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
                newTotal = clone.getFinalVictoryPoints() + getResourceHeuristic(clone, card);
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
            clone.getWonder().buildNextStage(this);
            newTotal = clone.getFinalVictoryPoints();
            if(newTotal>bestTotal){
                //best command yet, ship it!
                log.debug("Building wonder");
                setCurrentCommand(potentialCommand);
                bestTotal = newTotal;
            }
        }
    }

    private int getResourceHeuristic(Player player, Card card) {
        AssetMap assets = player.getOptionalAssetsSummary();
        assets.add(player.getAssets());
        AssetMap cardAssets = card.getAssets(player);
        AssetSet cardAssetsOptional = card.getAssetsOptional(player);
        int total = 0;
        List resources = Arrays.asList(Asset.TRADEABLE_ASSET_TYPES);
        for(String key: cardAssets.keySet()){
            if(resources.contains(key)){
                total+= 2*(2*cardAssets.get(key)-assets.get(key));
            }
        }

        for(String key: cardAssetsOptional){
            if(resources.contains(key)){
                total+= 2*(2*cardAssets.get(key)-assets.get(key));
            }
        }
        log.debug(card.getId()+": "+total);
        return total;
    }
}
