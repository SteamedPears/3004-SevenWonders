package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Asset;
import com.steamedpears.comp3004.models.Player;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class AssetView extends JPanel {
    public AssetView(Player player) {
        setLayout(new MigLayout(
                "aligny top", // Layout Constraints
                "0[]0[]0", // Column Constraints
                "0[]0[]0"  // Row Constraints
        ));
        for(String asset : Asset.AssetTypes) {
            add(new JLabel("" + Asset.getAsset(player.getAssets(),asset)),"w 70!");
        }
    }
}
