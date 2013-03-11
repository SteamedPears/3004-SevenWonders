package com.steamedpears.comp3004.models;

public class PlayerCommand{
    public enum PlayerCardAction{
        DISCARD, BUILD, PLAY, UNDISCARD, PLAY_FREE
    }

    /**
     * Gets the null command for a given player.  This command is to discard the first card in the player's hand, which
     * is always a valid move.
     * @param player the player whose null move to generate
     * @return the generated null move for the given player
     */
    public static PlayerCommand getNullCommand(Player player){
        PlayerCommand result = new PlayerCommand();
        result.action = PlayerCardAction.DISCARD;
        result.card = player.getHand().get(0).getId();

        return result;
    }

    public PlayerCardAction action;
    public String card;
    public AssetMap leftPurchases = new AssetMap();
    public AssetMap rightPurchases = new AssetMap();
    public PlayerCommand followup; //for if you can perform multiple actions this turn

    @Override
    public String toString() {
        return "PlayerCommand[" + this.action.toString() + " " + this.card + "]";
    }
}