package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.models.Card;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.*;

public class PlayerView extends JPanel {
    private List<CardView> cardViews;

    public PlayerView(Collection<Card> hand) {
        setLayout(new MigLayout());
        cardViews = new ArrayList<CardView>();
        for(Card c : hand) {
            cardViews.add(new CardView(c));
        }
        for(CardView cv : cardViews) {
            add(cv,"span");
        }
        // TODO: add some way to switch to high level view
    }
}
