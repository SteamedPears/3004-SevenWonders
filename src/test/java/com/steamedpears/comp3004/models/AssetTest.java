package com.steamedpears.comp3004.models;

import com.google.gson.JsonParser;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class AssetTest {

    JsonParser parser = new JsonParser();

    @Test
    public void constructorTest(){
        HashSet<String> expectedSet = new HashSet<String>();
        expectedSet.addAll(Arrays.asList(new String[]{Asset.ASSET_WOOD, Asset.ASSET_ORE}));

        HashMap<String, Integer> expectedMap = new HashMap<String, Integer>();
        expectedMap.put(Asset.ASSET_CLAY,2);
        expectedMap.put(Asset.ASSET_GLASS,1);

        assertEquals("AssetMap should be empty",
                new HashMap<String, Integer>(),
                new AssetMap());

        assertEquals("AssetSet should be empty",
                new HashSet<String>(),
                new AssetSet());

        assertEquals("AssetSet should have JSON's values",
                expectedSet,
                new AssetSet(parser.parse("[\"w\", \"o\"]").getAsJsonArray()));

        assertEquals("AssetMap should have JSON's values",
                expectedMap,
                new AssetMap(parser.parse("{\"g\":1,\"c\":2}").getAsJsonObject()));
    }

    @Test
    public void additionTest(){

        AssetMap map1 = new AssetMap();
        AssetMap map2 = new AssetMap();

        map1.add(Asset.ASSET_CLAY);
        assertEquals("Should have 1 clay",
                1,
                (int)map1.get(Asset.ASSET_CLAY));

        map2.add(Asset.ASSET_STONE, 5);
        assertEquals("Should have 5 stone",
                5,
                (int)map2.get(Asset.ASSET_STONE));

        HashMap<String, Integer> expectedSum = new HashMap<String, Integer>();
        expectedSum.put(Asset.ASSET_CLAY, 1);
        expectedSum.put(Asset.ASSET_STONE, 5);

        map2.add(map1);
        assertEquals("Sum should match",
                expectedSum,
                map2);

    }

    @Test
    public void removalTest(){
        AssetMap map1 = new AssetMap(parser.parse("{\"o\":1,\"c\":3}").getAsJsonObject());
        AssetMap map2 = new AssetMap(parser.parse("{\"o\":5,\"c\":1}").getAsJsonObject());

        map1.subtract(Asset.ASSET_CLAY);
        assertEquals("Should have 2 clay",
                2,
                (int)map1.get(Asset.ASSET_CLAY));

        map2.subtract(Asset.ASSET_ORE, 2);
        assertEquals("Should have 2 ore",
                3,
                (int)map2.get(Asset.ASSET_ORE));

        HashMap<String, Integer> expectedMap = new HashMap<String, Integer>();
        expectedMap.put(Asset.ASSET_CLAY, 1);

        map1.subtract(map2);
        assertEquals("Difference should match",
                expectedMap,
                map1);
    }
}
