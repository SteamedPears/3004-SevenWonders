package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;

public class TradesView extends JPanel {
    private PlayerTradesView leftPlayerTradesView;
    private PlayerTradesView rightPlayerTradesView;

    private String MIG_CONFIG = "span";

    public TradesView(Player player) {
        setLayout(new MigLayout("gap 0, inset 0"));

        add(new JLabel("Neighbor"));
        add(new AssetView(),MIG_CONFIG);

        add(new JLabel("Left"));
        leftPlayerTradesView = new PlayerTradesView(player,player.getPlayerLeft());
        add(leftPlayerTradesView,MIG_CONFIG);

        add(new JLabel("Right"));
        rightPlayerTradesView = new PlayerTradesView(player,player.getPlayerRight());
        add(rightPlayerTradesView,MIG_CONFIG);
    }

    public PlayerCommand getLeftCommand() { return leftPlayerTradesView.getCommand(); }
    public PlayerCommand getRightCommand() { return rightPlayerTradesView.getCommand(); }
}
