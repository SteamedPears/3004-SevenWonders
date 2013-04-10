package com.steamedpears.comp3004.models.players.strategies;

import com.steamedpears.comp3004.models.players.Player;
import com.steamedpears.comp3004.models.PlayerCommand;

public class NullStrategy implements Strategy {

    @Override
    public void handleTurn(Player player){
        player.setCurrentCommand(PlayerCommand.getNullCommand(player));
    }

    @Override
    public String toString() {
        return "Dumb";
    }
}
