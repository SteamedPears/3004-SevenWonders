package com.steamedpears.comp3004;

import java.util.Map;

public abstract class Asset {

    //resources
    public static final String ASSET_WOOD = "wood";
    public static final String ASSET_STONE = "stone";
    public static final String ASSET_CLAY = "clay";
    public static final String ASSET_ORE = "ore";

    //synthetics
    public static final String ASSET_LOOM = "loom";
    public static final String ASSET_PAPYRUS = "papyrus";
    public static final String ASSET_GLASS = "glass";

    //science
    public static final String ASSET_SCIENCE_1 = "science-1";
    public static final String ASSET_SCIENCE_2 = "science-2";
    public static final String ASSET_SCIENCE_3 = "science-3";

    //abilities
    public static final String ASSET_DISCARD = "discard";
    public static final String ASSET_DOUBLE_PLAY = "doublePlay";
    public static final String ASSET_BUILD_FREE = "buildFree";

    //misc
    public static final String ASSET_GOLD = "gold";
    public static final String ASSET_MILITARY = "military";
    public static final String ASSET_VICTORY = "victory";

    //utility methods
    public static int getAsset(Map<String, Integer> map, String key){
        if(map.containsKey(key)){
            return map.get(key);
        }else{
            return 0;
        }
    }

}
