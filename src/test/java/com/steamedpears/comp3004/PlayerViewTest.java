package com.steamedpears.comp3004;

import com.google.gson.*;
import com.steamedpears.comp3004.models.*;
import com.steamedpears.comp3004.views.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class PlayerViewTest extends JFrame {
    PlayerViewTest() throws Exception {
        setBounds(0,0,1500,800);
        JsonArray cards = (new JsonParser())
                .parse(new FileReader("src/main/resources/data/cards.json"))
                .getAsJsonObject()
                .get("cards")
                .getAsJsonArray();
        ArrayList<Card> hand = new ArrayList<Card>();
        for(int i = 0; i < 6; ++i) {
            hand.add(new Card(cards.get(i).getAsJsonObject()));
        }
        PlayerView view = new PlayerView(hand);
        add(view);
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new PlayerViewTest();
    }
}
