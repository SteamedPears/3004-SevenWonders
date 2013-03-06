package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.models.Card;

import com.steamedpears.comp3004.models.Player;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.*;

public class PlayerView extends JPanel {
    private List<CardView> cardViews;

    public PlayerView(Player player) {
        setLayout(new MigLayout());
        cardViews = new ArrayList<CardView>();
        for(Card c : player.getHand()) {
            cardViews.add(new CardView(c));
        }
        for(CardView cv : cardViews) {
            add(cv);
        }
        // TODO: add some way to switch to high level view
        // TODO: add tabular view of resources
        // TODO: add shield count
        // TODO: add gold count
        // TODO: add tabular view of sciences
        // TODO: add victory point count
    }
}
