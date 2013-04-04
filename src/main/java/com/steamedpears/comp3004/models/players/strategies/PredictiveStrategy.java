package com.steamedpears.comp3004.models.players.strategies;

import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.models.Wonder;
import com.steamedpears.comp3004.models.players.AIPlayer;

public class PredictiveStrategy implements Strategy {

    @Override
    public void handleTurn(Player player) {
        //TODO: implement this for better marks
        player.setCurrentCommand(PlayerCommand.getNullCommand(player));
    }
}
