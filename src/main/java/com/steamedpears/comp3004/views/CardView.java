package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Card;

import javax.swing.*;

public class CardView extends JLabel {
    private Card card;

    public CardView(Card card) {
        super(new ImageIcon(card.getImage()));
        this.card = card;
    }
}
