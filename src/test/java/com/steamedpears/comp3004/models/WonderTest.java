package com.steamedpears.comp3004.models;

import com.google.gson.JsonParser;
import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.routing.Router;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class WonderTest {

    Map<String, Wonder> wonders;

    static final String SOME_WONDER = "Gizah";

    @Before
    public void setup(){
        JsonParser parser = new JsonParser();
        wonders = Wonder.parseWonders(parser
                .parse(new InputStreamReader(JsonParser.class.getResourceAsStream(SevenWonders.PATH_WONDERS)))
                .getAsJsonObject()
                .get(Router.PROP_ROUTE_WONDERS)
                .getAsJsonArray());

        assertTrue(wonders.size()>0);
    }

    @Test
    public void testSideChange(){
        Wonder wonder = wonders.get(SOME_WONDER);

        String side = wonder.getSide();
        wonder.setSide(Wonder.PROP_WONDER_SIDE_B);
        assertNotEquals("Wonder side should be different",
                side,
                wonder.getSide());
    }

    @Test
    public void testBuild(){
        Wonder wonder = wonders.get(SOME_WONDER);
        Player player = mock(Player.class);
        AssetMap expectedInitialAssets = new AssetMap();
        expectedInitialAssets.add(Asset.ASSET_STONE);

        AssetMap originalAssets = wonder.getAssets(player);
        assertEquals("original assets should only be starting resource",
                expectedInitialAssets,
                wonder.getAssets(player));

        List<Card> stages = wonder.getStages();
        for(Card card: stages){
            assertEquals("Next stage should match list",
                    wonder.getNextStage(),
                    card);
            wonder.buildNextStage(player);
        }

        assertEquals("getNextStage should be null",
                null,
                wonder.getNextStage());

        assertNotEquals("assets should not still be original assets",
                expectedInitialAssets,
                wonder.getAssets(player));
    }


}
