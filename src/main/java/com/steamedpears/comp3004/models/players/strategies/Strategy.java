package com.steamedpears.comp3004.models.players.strategies;

import com.steamedpears.comp3004.models.Player;

public interface Strategy {
    /**
     * Try to find a move for the given player to take this turn. Any time a good move is found, the Strategy should
     * call player.setCurrentCommand to tell the player about it. This way, if the strategy times out or otherwise
     * fails to terminate properly, the Player will be able to do whatever the most recent move given to it was.
     * Ideally the Strategy should try to continuously refine the quality of the command given.
     * @param player the player to run the strategy on
     */
    public abstract void handleTurn(Player player);
}
