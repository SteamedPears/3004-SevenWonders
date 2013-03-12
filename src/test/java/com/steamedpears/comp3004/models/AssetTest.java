package com.steamedpears.comp3004.models;

import com.google.gson.JsonParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AssetTest {

    JsonParser parser = new JsonParser();

    @Test
    public void constructorTest(){
        HashSet<String> expectedSet = new HashSet<String>();
        expectedSet.addAll(Arrays.asList(Asset.ASSET_WOOD, Asset.ASSET_ORE));

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

    @Test
    public void validChoiceTest(){
        AssetSet set1 = new AssetSet();
        set1.addAll(Arrays.asList(Asset.ASSET_STONE,Asset.ASSET_CLAY,Asset.ASSET_GLASS));

        AssetSet set2 = new AssetSet();
        set2.addAll(Arrays.asList(Asset.ASSET_WOOD,Asset.ASSET_PAPYRUS,Asset.ASSET_CLAY));

        AssetSet set3 = new AssetSet();
        set3.addAll(Arrays.asList(Asset.ASSET_ORE,Asset.ASSET_STONE,Asset.ASSET_LOOM));

        AssetSet set4 = new AssetSet();
        set4.addAll(Arrays.asList(Asset.ASSET_WOOD,Asset.ASSET_STONE,Asset.ASSET_LOOM));

        AssetMap map = new AssetMap();
        map.add(Asset.ASSET_CLAY,2);
        map.add(Asset.ASSET_ORE);

        List<AssetSet> choices = new ArrayList<AssetSet>();
        choices.add(set1);
        choices.add(set2);

        assertFalse("Shoudn't be a valid choice",
                map.existsValidChoices(choices));

        choices.add(set3);

        assertTrue("Should be a valid choice",
                map.existsValidChoices(choices));

        choices.add(1, set4);

        assertTrue("Should still be a valid choice",
                map.existsValidChoices(choices));


    }
}
