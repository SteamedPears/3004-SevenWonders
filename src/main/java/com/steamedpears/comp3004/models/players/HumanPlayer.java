package com.steamedpears.comp3004.models.players;

import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.models.Wonder;
import org.apache.log4j.Logger;

public class HumanPlayer extends Player {
    private static Logger log = Logger.getLogger(HumanPlayer.class);
    public static final long TURN_LENGTH = 2*60*1000;

    public HumanPlayer(Wonder wonder, SevenWondersGame game){
        super(wonder, game);
    }

    @Override
    protected PlayerCommand handleTurn() {
        log.info("handleTurn called");
        try {
            Thread.sleep(TURN_LENGTH);
        } catch(InterruptedException e) {
            log.info("Player thread interrupted while waiting for input");
        }
        return PlayerCommand.getNullCommand(this);
    }
}
