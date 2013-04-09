package com.steamedpears.comp3004.models.players.strategies;

import com.steamedpears.comp3004.models.*;
import com.steamedpears.comp3004.models.assets.Asset;
import com.steamedpears.comp3004.models.assets.AssetMap;
import com.steamedpears.comp3004.models.assets.AssetSet;
import com.steamedpears.comp3004.models.players.HumanPlayer;
import com.steamedpears.comp3004.models.players.Player;
import com.steamedpears.comp3004.models.players.strategies.heuristics.Heuristic;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CollusionStrategy extends HeuristicStrategy {

    private static Logger log = Logger.getLogger(CollusionStrategy.class);
    private static final int roundsToDiscard = 1;

    public List<Heuristic> heuristics;
    private int roundsDiscarded = 0;

    public CollusionStrategy(){
        super();
    }

    public CollusionStrategy(Set<Integer> heuristicIndices){
        super(heuristicIndices);
    }

    @Override
    public void handleTurn(Player player){
        Player clone = player.clone(new CollusionStrategy());
        Player left = clone.getPlayerLeft().clone(new CollusionStrategy());
        if(clone.getPlayerLeft().getPlayerId()==0 || clone.getPlayerLeft() instanceof HumanPlayer){
            super.handleTurn(player);
        }else{
            PlayerCommand command = PlayerCommand.getNullCommand(clone);
            if(roundsDiscarded>=roundsToDiscard){
                for(Card card: clone.getHand()){
                    if(card.getColor().equals(Asset.COLOR_BROWN) || card.getColor().equals(Asset.COLOR_GREY)){
                        command = new PlayerCommand(PlayerCommand.PlayerCardAction.PLAY, card.getId());
                        roundsDiscarded=0;
                        break;
                    }
                }
            }else{
                roundsDiscarded++;
            }
            int total = 0;
            AssetMap tradeables = left.getAssetsTradeable();
            for(String key: tradeables.keySet()){
                int amount = Math.min((clone.getGold()-total)/2, tradeables.get(key));
                command.leftPurchases.add(key, amount);
                total+=amount;
                if((clone.getGold()-amount)/2<1){
                    break;
                }
            }

            player.setCurrentCommand(command);
        }
    }

}
