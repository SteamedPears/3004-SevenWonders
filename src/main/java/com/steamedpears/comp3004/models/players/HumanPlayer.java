package com.steamedpears.comp3004.models.players;

import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.models.Wonder;

public class HumanPlayer extends Player {

    public HumanPlayer(Wonder wonder, SevenWondersGame game){
        super(wonder, game);
    }

    @Override
    protected PlayerCommand handleTurn() {
        //TODO: implement this
        return PlayerCommand.getNullCommand(this);
    }
}
