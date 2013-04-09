package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.assets.*;
import static com.steamedpears.comp3004.views.AssetView.*;
import static com.steamedpears.comp3004.models.assets.Asset.*;

import net.miginfocom.swing.*;
import org.apache.log4j.*;
import javax.swing.*;
import java.util.*;

public class ComboAssetView extends JPanel {

    public final Map<String,String> mapAssetToIcon;

    static Logger log = Logger.getLogger(ComboAssetView.class);

    Map<AssetSet, Integer> assetCounts;

    public ComboAssetView(List<AssetSet> comboAssets) {
        setLayout(new MigLayout(
                "aligny top, alignx left, wrap 14, gap 0! 0!", // Layout Constraints
                "0[]0[]0", // Column Constraints
                "0[]1[]1[]1[]1[]1[]1[]1"  // Row Constraints
        ));

        mapAssetToIcon = new HashMap<String, String>();
        mapAssetToIcon.put(ASSET_WOOD,WOOD_ICON);
        mapAssetToIcon.put(ASSET_STONE,STONE_ICON);
        mapAssetToIcon.put(ASSET_CLAY,CLAY_ICON);
        mapAssetToIcon.put(ASSET_ORE,ORE_ICON);
        mapAssetToIcon.put(ASSET_LOOM,LOOM_ICON);
        mapAssetToIcon.put(ASSET_PAPYRUS,PAPYRUS_ICON);
        mapAssetToIcon.put(ASSET_GLASS,GLASS_ICON);
        mapAssetToIcon.put(ASSET_SCIENCE_1,SCIENCE1_ICON);
        mapAssetToIcon.put(ASSET_SCIENCE_2,SCIENCE2_ICON);
        mapAssetToIcon.put(ASSET_SCIENCE_3,SCIENCE3_ICON);

        assetCounts = new HashMap<AssetSet, Integer>();

        for(AssetSet set : comboAssets) {
            if(!assetCounts.containsKey(set)) {
                assetCounts.put(set,1);
            } else {
                assetCounts.put(set,assetCounts.get(set)+1);
            }
        }

        boolean firstSet = true;
        for(AssetSet set : assetCounts.keySet()) {
            if(!firstSet) {
                add(new JLabel(","));
            }
            firstSet = false;
            int count = assetCounts.get(set);
            JPanel assetPanel = new JPanel();
            assetPanel.setLayout(new MigLayout(
                    "aligny top, alignx left, wrap 14, gap 0! 0!", // Layout Constraints
                    "0[]0[]0", // Column Constraints
                    "0[]1[]1[]1[]1[]1[]1[]1"  // Row Constraints
            ));
            boolean firstAsset = true;
            for(String s : set) {
                if(!firstAsset) {
                    assetPanel.add(new JLabel("/"));
                }
                firstAsset = false;
                assetPanel.add(AssetView.newJLabel(mapAssetToIcon.get(s), 60),AssetView.MIG_CONFIG);
            }
            add(assetPanel);
            add(new JLabel(": " + count));
            log.info("Built asset panel for " + set + ":" + count);
        }
    }
}
