package com.steamedpears.comp3004.models.players;

import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.models.Wonder;
import com.steamedpears.comp3004.models.players.strategies.Strategy;

public class AIPlayer extends Player {
    private Strategy strategy;

    public AIPlayer(Wonder wonder, SevenWondersGame game, Strategy strategy){
        super(wonder, game);
        this.strategy = strategy;
    }

    @Override
    protected void handleTurn() {
        strategy.handleTurn(this);
    }
}
