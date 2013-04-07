package com.steamedpears.comp3004.models.players.strategies;

import com.steamedpears.comp3004.models.players.Player;
import com.steamedpears.comp3004.models.PlayerCommand;

public class PredictiveStrategy implements Strategy {

    @Override
    public void handleTurn(Player player) {
        //TODO: implement this for better marks
        player.setCurrentCommand(PlayerCommand.getNullCommand(player));
    }
}
