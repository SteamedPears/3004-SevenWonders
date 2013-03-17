package com.steamedpears.comp3004.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.routing.Router;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    static final String CARD_NAME_COST_RESOURCE = "University";
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
        when(brokePlayer.getPlayerLeft()).thenReturn(brokePlayer);
        when(brokePlayer.getPlayerRight()).thenReturn(brokePlayer);

        richPlayer = mock(Player.class);
        AssetMap allTheAssets = new AssetMap();
        for(String asset: Asset.ASSET_TYPES){
            allTheAssets.add(asset, 100);
        }
        for(String color: Asset.COLORS){
            allTheAssets.add(color, 100);
        }
        when(richPlayer.getConcreteAssets()).thenReturn(allTheAssets);
        when(richPlayer.getAssets()).thenReturn(allTheAssets);
        when(richPlayer.getOptionalAssetsComplete()).thenReturn(new ArrayList<AssetSet>());
        when(richPlayer.getPlayerLeft()).thenReturn(richPlayer);
        when(richPlayer.getPlayerRight()).thenReturn(richPlayer);

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

        assertTrue("Rich player should be able to afford this cardID",
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

    @Test
    public void testMultiplier(){
        AssetMap multipliedMap = new AssetMap();
        multipliedMap.add(Asset.ASSET_VICTORY_POINTS, 100*2);

        assertEquals("Card should have multiplied value",
                multipliedMap,
                cards.get(CARD_NAME_MULTIPLIER).getAssets(richPlayer));

        assertEquals("Card should not give player any assets",
                new AssetMap(),
                cards.get(CARD_NAME_MULTIPLIER).getAssets(brokePlayer));
    }

    @Test
    public void testTrade(){
        assertFalse("Shouldn't be able to afford it",
                cards.get(CARD_NAME_COST_RESOURCE).canAfford(brokePlayer, new PlayerCommand()));

        PlayerCommand command = new PlayerCommand();
        command.leftPurchases.add(Asset.ASSET_WOOD);
        command.leftPurchases.add(Asset.ASSET_PAPYRUS);
        command.rightPurchases.add(Asset.ASSET_GLASS);
        command.rightPurchases.add(Asset.ASSET_WOOD);

        assertTrue("Should be able to afford it",
                cards.get(CARD_NAME_COST_RESOURCE).canAfford(brokePlayer, command));

    }

    @Test
    public void testDiscount(){
        Set<String> expectedTargets = new HashSet<String>();
        expectedTargets.add(Card.PLAYER_RIGHT);

        AssetSet expectedDiscounts = new AssetSet();
        String[] assets = {Asset.ASSET_ORE, Asset.ASSET_STONE, Asset.ASSET_WOOD, Asset.ASSET_CLAY};
        expectedDiscounts.addAll(Arrays.asList(assets));

        assertEquals("cardID should not have discount targets",
                new HashSet<String>(),
                cards.get(CARD_NAME_FREE).getDiscountsTargets());

        assertEquals("cardID should not have discount assets",
                new AssetSet(),
                cards.get(CARD_NAME_FREE).getDiscountsAssets());

        assertEquals("cardID should have discount targets",
                expectedTargets,
                cards.get(CARD_NAME_TRADES).getDiscountsTargets());

        assertEquals("cardID should have discount assets",
                expectedDiscounts,
                cards.get(CARD_NAME_TRADES).getDiscountsAssets());
    }
}
