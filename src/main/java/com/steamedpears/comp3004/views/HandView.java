package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.models.Card;

import net.miginfocom.swing.MigLayout;
import com.google.gson.*;

import javax.swing.*;
import java.util.*;
import java.io.*;

public class HandView extends JPanel {
    private List<CardView> cardViews;

    public HandView(Collection<Card> hand) {
        setLayout(new MigLayout());
        cardViews = new ArrayList<CardView>();
        for(Card c : hand) {
            cardViews.add(new CardView(c));
        }
        for(CardView cv : cardViews) {
            add(cv);
        }
        // TODO: add some way to switch to high level view
    }

    // view testing
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        JsonArray cards = (new JsonParser())
                .parse(new FileReader("src/main/resources/data/cards.json"))
                .getAsJsonObject()
                .get("cards")
                .getAsJsonArray();
        ArrayList<Card> hand = new ArrayList<Card>();
        for(int i = 0; i < 6; ++i) {
            hand.add(new Card(cards.get(i).getAsJsonObject()));
        }
        HandView view = new HandView(hand);
        frame.add(view);
        frame.setVisible(true);
    }
}
