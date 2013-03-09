package com.steamedpears.comp3004.views;

import com.google.gson.*;
import com.steamedpears.comp3004.models.*;
import org.apache.log4j.*;

import static org.mockito.Mockito.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class PlayerViewTest extends JFrame {
    PlayerViewTest() throws Exception {
        // initialization
        setBounds(0,0,1500,800);

        // build hand
        JsonArray cards = (new JsonParser())
                .parse(new FileReader("src/main/resources/data/cards.json"))
                .getAsJsonObject()
                .get("cards")
                .getAsJsonArray();
        ArrayList<Card> hand = new ArrayList<Card>();
        for(int i = 0; i < 6; ++i) {
            hand.add(new Card(cards.get(i).getAsJsonObject()));
        }

        // build player
        Player player = mock(Player.class);
        when(player.getHand()).thenReturn(hand);
        when(player.getMilitaryResults()).thenReturn(Arrays.asList(2,3,-5,1));
        when(player.getGold()).thenReturn(42);

        // create view
        PlayerView view = new PlayerView(player);
        add(view);
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        new PlayerViewTest();
    }
}
