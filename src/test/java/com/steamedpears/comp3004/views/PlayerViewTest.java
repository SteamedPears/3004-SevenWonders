package com.steamedpears.comp3004.views;

import com.google.gson.*;
import com.steamedpears.comp3004.models.*;
import com.steamedpears.comp3004.models.assets.Asset;
import com.steamedpears.comp3004.models.assets.AssetMap;
import com.steamedpears.comp3004.models.assets.AssetSet;
import com.steamedpears.comp3004.models.players.Player;
import org.apache.log4j.*;
import org.junit.Ignore;

import static org.mockito.Mockito.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

@Ignore
public class PlayerViewTest extends JFrame {
    PlayerViewTest() throws Exception {
        // initialization
        setBounds(0,0,960,560);

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

        // build resources
        AssetMap assets = new AssetMap();
        int i = 0;
        for(String assetType : Asset.ASSET_TYPES) {
            assets.put(assetType,++i);
        }

        // build optional assets
        List<AssetSet> optionalAssets = new ArrayList<AssetSet>();
        AssetSet set = new AssetSet();
        set.add(Asset.ASSET_CLAY);
        set.add(Asset.ASSET_ORE);
        optionalAssets.add(set);
        set = new AssetSet();
        set.add(Asset.ASSET_CLAY);
        set.add(Asset.ASSET_ORE);
        optionalAssets.add(set);
        set = new AssetSet();
        set.add(Asset.ASSET_CLAY);
        set.add(Asset.ASSET_ORE);
        set.add(Asset.ASSET_STONE);
        set.add(Asset.ASSET_WOOD);
        optionalAssets.add(set);

        // build game
        SevenWondersGame game = mock(SevenWondersGame.class);
        when(game.getAge()).thenReturn(2);

        // build player
        Player player = mock(Player.class);
        when(player.getHand()).thenReturn(hand);
        when(player.getAssets()).thenReturn(assets);
        when(player.isValid(any(PlayerCommand.class))).thenReturn(true);
        when(player.getGame()).thenReturn(game);
        when(player.getOptionalAssetsComplete()).thenReturn(optionalAssets);

        // create view
        PlayerView view = new PlayerView(player);
        add(view);

        view.addMessage("One","One");
        view.addMessage("Two","Two");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        new PlayerViewTest();
    }
}
