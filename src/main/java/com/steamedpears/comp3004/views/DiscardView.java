package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.SevenWondersGame;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;
import java.util.Set;

public class DiscardView extends JPanel {
    private static final int SELECTED_MULTIPLIER = 3;

    private SevenWondersGame game;
    private CardView selectedCardView;

    public DiscardView(SevenWondersGame game) {
        this.game = game;
        setLayout(new MigLayout("wrap 9"));
        update();
    }

    /**
     * Update the view.
     */
    public void update() {
        removeAll();
        selectedCardView = null;

        Set<Card> discardedCards = game.getDiscard();
        if(discardedCards.size() > 0) {
            selectedCardView = new CardView((Card)discardedCards.toArray()[0],
                    CardView.DEFAULT_WIDTH * SELECTED_MULTIPLIER);
            add(selectedCardView,"span " + SELECTED_MULTIPLIER + " " + SELECTED_MULTIPLIER);

            for(Card c : discardedCards) {
                CardView cv = new CardView(c);
                cv.setSelectionListener(new CardSelectionListener() {
                    @Override
                    public void handleSelection(Card card) {
                        selectedCardView.setCard(card);
                    }
                });
                add(cv);
            }
        }

    }
}
