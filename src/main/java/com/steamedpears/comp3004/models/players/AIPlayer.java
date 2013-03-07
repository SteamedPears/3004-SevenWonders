package com.steamedpears.comp3004.models.players;

import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.models.Wonder;
import com.steamedpears.comp3004.views.NewGameDialog;

public abstract class AIPlayer extends Player {
    public AIPlayer(Wonder wonder, SevenWondersGame game){
        super(wonder, game);
    }

    @Override
    protected void handleTurn() {
        setCurrentCommand(PlayerCommand.getNullCommand(this));
    }
}
