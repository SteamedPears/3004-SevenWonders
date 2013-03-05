package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.models.Card;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class HandView extends JPanel {
    private List<CardView> cardViews;
    SevenWonders controller;

    public HandView(SevenWonders controller) {
        setLayout(new MigLayout());
        this.controller = controller;
        cardViews = new ArrayList<CardView>();
        for(Card c : controller.getHand()) {
            cardViews.add(new CardView(c));
        }
        for(CardView cv : cardViews) {
            add(cv);
        }
        // TODO: add some way to switch to high level view
    }
}
