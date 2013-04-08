package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.assets.*;
import static com.steamedpears.comp3004.views.AssetView.*;
import com.steamedpears.comp3004.models.assets.Asset;

import net.miginfocom.swing.*;
import org.apache.log4j.*;
import javax.swing.*;
import java.util.*;

public class ComboAssetView extends JPanel {

    public static final char ASSET_WOOD = 'w';
    public static final char ASSET_STONE = 's';
    public static final char ASSET_CLAY = 'c';
    public static final char ASSET_ORE = 'o';

    public static final char ASSET_LOOM = 'l';
    public static final char ASSET_PAPYRUS = 'p';
    public static final char ASSET_GLASS = 'g';

    static Logger log = Logger.getLogger(ComboAssetView.class);

    Map<AssetSet, Integer> assetCounts;

    public ComboAssetView(List<AssetSet> comboAssets) {
        setLayout(new MigLayout(
                "aligny top,wrap 14, gap 0", // Layout Constraints
                "0[]0[]0", // Column Constraints
                "0[]1[]1[]1[]1[]1[]1[]1"  // Row Constraints
        ));

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
                    "aligny top,wrap 14, gap 0", // Layout Constraints
                    "0[]0[]0", // Column Constraints
                    "0[]0[]0[]0[]0[]0[]0[]0"  // Row Constraints
            ));
            boolean firstAsset = true;
            for(String s : set) {
                if(!firstAsset) {
                    assetPanel.add(new JLabel("/"));
                }
                firstAsset = false;
                switch(s.charAt(0)) {
                    case ASSET_WOOD:
                        assetPanel.add(AssetView.newJLabel(WOOD_ICON));
                        break;
                    case ASSET_STONE:
                        assetPanel.add(AssetView.newJLabel(STONE_ICON));
                        break;
                    case ASSET_CLAY:
                        assetPanel.add(AssetView.newJLabel(CLAY_ICON));
                        break;
                    case ASSET_ORE:
                        assetPanel.add(AssetView.newJLabel(ORE_ICON));
                        break;
                    case ASSET_LOOM:
                        assetPanel.add(AssetView.newJLabel(LOOM_ICON));
                        break;
                    case ASSET_PAPYRUS:
                        assetPanel.add(AssetView.newJLabel(PAPYRUS_ICON));
                        break;
                    case ASSET_GLASS:
                        assetPanel.add(AssetView.newJLabel(GLASS_ICON));
                        break;
                    default:
                        log.error("Unknown asset type!!!");
                        System.exit(-1);
                        break;
                }
            }
            add(assetPanel);
            add(new JLabel(": " + count));
            log.info("Built asset panel for " + set + ":" + count);
        }
    }
}
