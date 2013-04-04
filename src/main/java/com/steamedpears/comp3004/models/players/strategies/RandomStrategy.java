package com.steamedpears.comp3004.models.players.strategies;

import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.models.Wonder;
import com.steamedpears.comp3004.models.players.AIPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomStrategy implements Strategy {

    @Override
    public void handleTurn(Player player) {
        List<Card> hand = player.getHand();
        PlayerCommand command = new PlayerCommand();

        //get a random order to try the cards in
        List<Integer> orderOfChoices = new ArrayList<Integer>();
        for(int i=0; i<hand.size(); ++i){
            orderOfChoices.add(i);
        }

        Collections.shuffle(orderOfChoices);

        //just try to play any card
        for(int choice: orderOfChoices){
            Card curCard = hand.get(choice);
            if(curCard.canAfford(player, command)){
                command.cardID = curCard.getId();
                command.action = PlayerCommand.PlayerCardAction.PLAY;
                player.setCurrentCommand(command);
                return;
            }
        }

        //can't play any card, try to build wonder
        if(!player.hasFinishedWonder()){
            Card nextStage = player.getWonder().getNextStage();
            if(nextStage.canAfford(player, command)){
                command.cardID = hand.get(orderOfChoices.get(0)).getId();
                command.action = PlayerCommand.PlayerCardAction.BUILD;
                player.setCurrentCommand(command);
                return;
            }
        }

        //can't build, just discard
        command.cardID = hand.get(orderOfChoices.get(0)).getId();
        command.action = PlayerCommand.PlayerCardAction.DISCARD;
        player.setCurrentCommand(command);
    }

}
