package com.steamedpears.comp3004.models.players.strategies.heuristics;

import com.steamedpears.comp3004.models.*;
import com.steamedpears.comp3004.models.assets.AssetMap;
import com.steamedpears.comp3004.models.assets.AssetSet;
import com.steamedpears.comp3004.models.players.Player;

import java.util.Arrays;
import java.util.List;

import static com.steamedpears.comp3004.models.assets.Asset.*;

public class ScienceHeuristic extends Heuristic{

    @Override
    public int getHeuristic(Player player, PlayerCommand command){
        int heuristic = 0;
        Card card = getAddedCard(player, command);
        if(card==null){
            return heuristic;
        }

        AssetSet cardAssetsOptional = card.getAssetsOptional(player);
        AssetMap cardAssets = card.getAssets(player);
        AssetMap assets = player.getOptionalAssetsSummary();
        assets.add(player.getAssets());
        List sciences = Arrays.asList(ASSET_SCIENCE_1, ASSET_SCIENCE_2, ASSET_SCIENCE_3);

        for(String key: cardAssets.keySet()){
            if(sciences.contains(key)){
                heuristic+=3;
            }
        }

        for(String key: cardAssetsOptional){
            if(sciences.contains(key)){
                heuristic+=3;
            }
        }
        return heuristic;
    }

    @Override
    public String getName() {
        return "Science Heuristic";
    }

    @Override
    public String getDescription() {
        return "Approximates the value of science yielded by an action";
    }
}
