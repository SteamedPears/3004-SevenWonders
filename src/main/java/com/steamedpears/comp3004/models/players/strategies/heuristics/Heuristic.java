package com.steamedpears.comp3004.models.players.strategies.heuristics;

import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.players.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Heuristic {
    static List<Heuristic> heuristics = new ArrayList<Heuristic>();

    static{
        heuristics.add(new VictoryPointHeuristic());
        heuristics.add(new MilitaryHeuristic());
        heuristics.add(new ResourceHeuristic());
        heuristics.add(new ScienceHeuristic());
    }

    public static List<Heuristic> getHeuristics(){
        return heuristics;
    }

    /**
     * Get the card the given command would yield the given player
     * @param player the player playing the card
     * @param command the command for the player to perform
     * @return the card yielded
     */
    public static Card getAddedCard(Player player, PlayerCommand command){
        if(command.action.equals(PlayerCommand.PlayerCardAction.BUILD)){
            return player.getWonder().getNextStage();
        }else if(command.action.equals(PlayerCommand.PlayerCardAction.DISCARD)){
            return null;
        }else{
            return player.getGame().getCardById(command.cardID);
        }
    }

    /**
     * Get the value the value in victory points this heuristic believes is yielded by the given player performing
     * the given command
     * @param player the player performing the command
     * @param command the command for the player to perform
     * @return the victory points yielded
     */
    public abstract int getHeuristic(Player player, PlayerCommand command);

    /**
     * Get the name of the heuristic
     * @return the name of the heuristic
     */
    public abstract String getName();

    /**
     * Get the description of the heuristic
     * @return the description of the heuristic
     */
    public abstract String getDescription();
}
