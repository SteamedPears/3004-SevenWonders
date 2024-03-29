package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Card;

public abstract class CardSelectionListener {

    /**
     * A method for dealing with the selection of a card.
     * @param card The card that was selected.
     */
    public abstract void handleSelection(Card card);
}
