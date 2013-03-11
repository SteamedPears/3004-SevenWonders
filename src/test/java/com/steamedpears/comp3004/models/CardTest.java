package com.steamedpears.comp3004.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.routing.Router;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CardTest {

    List<Card> deck;
    JsonArray cardJSON;

    static final String CARD_NAME_FREE = "Lumber Yard";
    static final String CARD_NAME_COST_MONEY = "Tree Farm";
    static final String CARD_NAME_CHOICE = "Tree Farm";
    static final String CARD_NAME_MULTIPLIER = "Workers Guild";
    static final String CARD_NAME_GAIN_MONEY = "Vineyard";
    static final String CARD_NAME_TRADES = "East Trading Post";

    Map<String, Card> cards;

    Player brokePlayer;
    Player richPlayer;

    @Before
    public void setup(){
        JsonParser parser = new JsonParser();
        this.cardJSON = parser
                .parse(new InputStreamReader(CardTest.class.getResourceAsStream(SevenWonders.PATH_CARDS)))
                .getAsJsonObject()
                .get(Router.PROP_ROUTE_CARDS)
                .getAsJsonArray();

        deck = Card.parseDeck(cardJSON);

        cards = new HashMap<String, Card>();
        for(Card card: deck){
            cards.put(card.getName(), card);
        }

        brokePlayer = mock(Player.class);
        when(brokePlayer.getConcreteAssets()).thenReturn(new AssetMap());
        when(brokePlayer.getAssets()).thenReturn(new AssetMap());
        when(brokePlayer.getOptionalAssetsComplete()).thenReturn(new ArrayList<AssetSet>());

        richPlayer = mock(Player.class);
        AssetMap allTheAssets = new AssetMap();
        for(String asset: Asset.AssetTypes){
            allTheAssets.add(asset, 100);
        }
        when(richPlayer.getConcreteAssets()).thenReturn(allTheAssets);
        when(richPlayer.getAssets()).thenReturn(allTheAssets);
        when(richPlayer.getOptionalAssetsComplete()).thenReturn(new ArrayList<AssetSet>());

        System.out.println(richPlayer.getConcreteAssets());
    }

    @Test
    public void testCardsLoaded(){
        assertTrue(cards.containsKey(CARD_NAME_FREE));
        assertTrue(cards.containsKey(CARD_NAME_COST_MONEY));
        assertTrue(cards.containsKey(CARD_NAME_CHOICE));
        assertTrue(cards.containsKey(CARD_NAME_GAIN_MONEY));
        assertTrue(cards.containsKey(CARD_NAME_MULTIPLIER));
        assertTrue(cards.containsKey(CARD_NAME_TRADES));
    }

    @Test
    public void testPlayFree(){
        assertTrue("Free cards should always be playable",
                cards.get(CARD_NAME_FREE).canAfford(brokePlayer, new PlayerCommand()));
    }

    @Test
    public void testChargeGold(){
        assertFalse("Broke player shouldn't be able to pay for this",
                cards.get(CARD_NAME_COST_MONEY).canAfford(brokePlayer, new PlayerCommand()));

        assertTrue("Rich player should be able to afford this card",
                cards.get(CARD_NAME_COST_MONEY).canAfford(richPlayer, new PlayerCommand()));



        Player player = Player.newAIPlayer(null, null);
        int oldMoney = player.getGold();
        cards.get(CARD_NAME_COST_MONEY).playCard(player);

        assertEquals("Player should have one less coin",
                oldMoney-1,
                player.getGold());
    }

    @Test
    public void testChoice(){
        String[] expectedAssets = {Asset.ASSET_WOOD, Asset.ASSET_CLAY};
        AssetSet expectedSet = new AssetSet();
        expectedSet.addAll(Arrays.asList(expectedAssets));

        assertEquals("Card without options should not give any options",
                new AssetSet(),
                cards.get(CARD_NAME_FREE).getAssetsOptional(richPlayer));

        assertEquals("Card with options should not give any assets",
                new AssetMap(),
                cards.get(CARD_NAME_CHOICE).getAssets(richPlayer));

        assertEquals("Card didn't have right options",
                expectedSet,
                cards.get(CARD_NAME_CHOICE).getAssetsOptional(richPlayer));
    }
}
