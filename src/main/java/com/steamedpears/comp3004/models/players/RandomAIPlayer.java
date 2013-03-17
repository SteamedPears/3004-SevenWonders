package com.steamedpears.comp3004.models.players;

import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.models.Wonder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomAIPlayer extends AIPlayer{
    public RandomAIPlayer(Wonder wonder, SevenWondersGame game){
        super(wonder, game);
    }

    @Override
    protected void handleTurn() {
        List<Card> hand = getHand();
        PlayerCommand command = new PlayerCommand();

        //get a random order to try the cards in
        List<Integer> orderOfChoices = new ArrayList<Integer>();
        for(int i=0; i<hand.size(); ++i){
            orderOfChoices.add(i);
        }

        Collections.shuffle(orderOfChoices);

        //just try to play any cardID
        for(int choice: orderOfChoices){
            Card curCard = hand.get(choice);
            if(curCard.canAfford(this, command)){
                command.cardID = curCard.getId();
                command.action = PlayerCommand.PlayerCardAction.PLAY;
                setCurrentCommand(command);
                return;
            }
        }

        //can't play any cardID, try to build wonder
        if(!hasFinishedWonder()){
            Card nextStage = getWonder().getNextStage();
            if(nextStage.canAfford(this, command)){
                command.cardID = hand.get(orderOfChoices.get(0)).getId();
                command.action = PlayerCommand.PlayerCardAction.BUILD;
                setCurrentCommand(command);
                return;
            }
        }

        //can't build, just discard
        command.cardID = hand.get(orderOfChoices.get(0)).getId();
        command.action = PlayerCommand.PlayerCardAction.DISCARD;
        setCurrentCommand(command);
    }

}
