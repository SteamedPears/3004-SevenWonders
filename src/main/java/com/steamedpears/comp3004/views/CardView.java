package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.models.Card;

import com.google.gson.*;

import javax.swing.*;
import java.io.FileReader;

public class CardView extends JLabel {
    private Card card;

    public CardView(Card card) {
        super(new ImageIcon(card.getImage()));
        this.card = card;
    }

    // view testing
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        JsonArray cards = (new JsonParser())
                .parse(new FileReader("src/main/resources/data/cards.json"))
                .getAsJsonObject()
                .get("cards")
                .getAsJsonArray();
        Card testCard = new Card(cards.get(0).getAsJsonObject());
        CardView view = new CardView(testCard);
        frame.add(view);
        frame.setVisible(true);
    }
}
