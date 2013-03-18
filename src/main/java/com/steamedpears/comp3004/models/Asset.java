package com.steamedpears.comp3004.models;

public final class Asset {

    //prevent making this real
    private Asset(){}

    //resources
    public static final String ASSET_WOOD =     "w";
    public static final String ASSET_STONE =    "s";
    public static final String ASSET_CLAY =     "c";
    public static final String ASSET_ORE =      "o";

    //synthetics
    public static final String ASSET_LOOM =     "l";
    public static final String ASSET_PAPYRUS =  "p";
    public static final String ASSET_GLASS =    "g";

    //science
    public static final String ASSET_SCIENCE_1 = "tab";
    public static final String ASSET_SCIENCE_2 = "comp";
    public static final String ASSET_SCIENCE_3 = "gear";

    //abilities
    public static final String ASSET_DISCARD =      "discard";
    public static final String ASSET_DOUBLE_PLAY =  "lastcard";
    public static final String ASSET_BUILD_FREE =   "freeBuild";
    public static final String ASSET_GUILD_COPY =   "guildCopy";

    //misc
    public static final String ASSET_GOLD =             "coin";
    public static final String ASSET_VICTORY_POINTS =   "victory";
    public static final String ASSET_MILITARY_POWER =   "army";
    public static final String ASSET_MILITARY_DEFEAT =  "defeat";
    public static final String ASSET_MILITARY_VICTORY = "win";
    public static final String ASSET_WONDER_STAGES =    "wonderstages";

    //colors
    public static final String COLOR_RED =          "red";
    public static final String COLOR_YELLOW =       "yellow";
    public static final String COLOR_GREY =         "gray";
    public static final String COLOR_PURPLE =       "purple";
    public static final String COLOR_GREEN =        "green";
    public static final String COLOR_BROWN =        "brown";

    // for enumeration
    public static final String[] ASSET_TYPES = {
            ASSET_WOOD, ASSET_STONE, ASSET_CLAY, ASSET_ORE, ASSET_LOOM, ASSET_PAPYRUS, ASSET_GLASS,
            ASSET_SCIENCE_1, ASSET_SCIENCE_2, ASSET_SCIENCE_3, ASSET_GOLD, ASSET_VICTORY_POINTS,
            ASSET_MILITARY_POWER, ASSET_WONDER_STAGES
    };

    public static final String[] TRADEABLE_ASSET_TYPES = {
            ASSET_WOOD, ASSET_STONE, ASSET_CLAY, ASSET_ORE, ASSET_LOOM, ASSET_PAPYRUS, ASSET_GLASS
    };

    public static final String[] COLORS = {
            COLOR_BROWN, COLOR_GREEN, COLOR_GREY, COLOR_PURPLE, COLOR_RED, COLOR_YELLOW
    };

}
