package com.steamedpears.comp3004.models;

import java.util.Map;

public final class Asset {

    //prevent making this real
    private Asset(){}

    //resources
    public static final String ASSET_WOOD = "w";
    public static final String ASSET_STONE = "s";
    public static final String ASSET_CLAY = "c";
    public static final String ASSET_ORE = "o";

    //synthetics
    public static final String ASSET_LOOM = "l";
    public static final String ASSET_PAPYRUS = "p";
    public static final String ASSET_GLASS = "g";

    //science
    public static final String ASSET_SCIENCE_1 = "tab";
    public static final String ASSET_SCIENCE_2 = "comp";
    public static final String ASSET_SCIENCE_3 = "gear";

    //abilities
    public static final String ASSET_DISCARD = "discard";
    public static final String ASSET_DOUBLE_PLAY = "lastcard";
    public static final String ASSET_BUILD_FREE = "freeBuild";
    public static final String ASSET_GUILD_COPY = "guildCopy";

    //misc
    public static final String ASSET_GOLD = "gold";
    public static final String ASSET_MILITARY = "army";
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
