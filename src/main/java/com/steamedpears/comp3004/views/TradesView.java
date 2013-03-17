package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Asset;
import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;

public class TradesView extends JPanel {
    private Player player;

    private String MIG_CONFIG = "w 60!";

    public TradesView(Player player) {
        this.player = player;
        setLayout(new MigLayout("gap 0, inset 0"));
        update();
    }

    public void update() {
        removeAll();

        add(new JLabel("Neighbor"));
        add(new AssetView(),"span");

        add(new JLabel("Left"));
        Player leftNeighbor = player.getPlayerLeft();
        add(new AssetView(leftNeighbor.getAssetsTradeable(),
                leftNeighbor.getOptionalAssetsCompleteTradeable()),
                "span");

        add(new JLabel(""));
        for(String s : Asset.TRADEABLE_ASSET_TYPES) {
            add(new JTextField("0"),MIG_CONFIG);
        }

        add(new JLabel("Right"),"newline");
        Player rightNeighbor = player.getPlayerRight();
        add(new AssetView(rightNeighbor.getAssetsTradeable(),
                rightNeighbor.getOptionalAssetsCompleteTradeable()),
                "span");

        add(new JLabel(""),"newline");
        for(String s : Asset.TRADEABLE_ASSET_TYPES) {
            add(new JTextField("0"),MIG_CONFIG);
        }
    }
}
