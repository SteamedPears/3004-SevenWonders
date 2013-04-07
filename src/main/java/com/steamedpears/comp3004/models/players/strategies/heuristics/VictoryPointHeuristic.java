package com.steamedpears.comp3004.models.players.strategies.heuristics;

import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.Wonder;
import com.steamedpears.comp3004.models.players.Player;

public class VictoryPointHeuristic extends Heuristic{

    @Override
    public int getHeuristic(Player player, PlayerCommand command){
        int heuristic = 0;

        Card card = player.getGame().getCardById(command.cardID);

        Wonder oldWonder = null;
        if(command.action.equals(PlayerCommand.PlayerCardAction.BUILD)){
            oldWonder = player.getWonder().getClone();
            player.getWonder().buildNextStage(player);
        }else if(command.action.equals(PlayerCommand.PlayerCardAction.DISCARD)){
            player.changeGold(3);
        }else{
            player.getPlayedCards().add(card);
        }

        int tradeCost = player.getCostOfTrade(command.leftPurchases,player.getDiscounts(player.getPlayerLeft()))
                + player.getCostOfTrade(command.rightPurchases,player.getDiscounts(player.getPlayerRight()));
        player.changeGold(-tradeCost);

        heuristic += player.getFinalVictoryPoints();

        player.changeGold(tradeCost);

        if(command.action.equals(PlayerCommand.PlayerCardAction.BUILD)){
            player.setWonder(oldWonder);
        }else if(command.action.equals(PlayerCommand.PlayerCardAction.DISCARD)){
            player.changeGold(-3);
        }else{
            player.getPlayedCards().remove(card);
        }

        return heuristic;
    }

    @Override
    public String getName() {
        return "Victory Point Heuristic";
    }

    @Override
    public String getDescription() {
        return "Computes the final victory points the player would have if the game ended after this action";
    }
}
