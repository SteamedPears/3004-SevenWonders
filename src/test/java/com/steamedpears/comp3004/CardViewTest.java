package com.steamedpears.comp3004;
import com.steamedpears.comp3004.models.*;
import com.steamedpears.comp3004.views.*;

import com.google.gson.*;

import javax.swing.*;
import java.io.*;

public class CardViewTest extends JFrame {
    public CardViewTest() throws Exception {
        setBounds(0, 0, 1000, 1000);
        JsonArray cards = (new JsonParser())
                .parse(new FileReader("src/main/resources/data/cards.json"))
                .getAsJsonObject()
                .get("cards")
                .getAsJsonArray();
        Card testCard = new Card(cards.get(0).getAsJsonObject());
        CardView view = new CardView(testCard);
        add(view);
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new CardViewTest();
    }
}