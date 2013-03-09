package com.steamedpears.comp3004.views;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class AssetHeaderView extends JPanel {
    private static String config = "w 70!, aligny top";

    public AssetHeaderView() {
        setLayout(new MigLayout(
                "aligny top", // Layout Constraints
                "0[]0[]0", // Column Constraints
                "0[]0[]0"  // Row Constraints
        ));

        // TODO: replace these with icons
        add(new JLabel("Wood"),config);
        add(new JLabel("Stone"),config);
        add(new JLabel("Clay"),config);
        add(new JLabel("Ore"),config);
        add(new JLabel("Loom"),config);
        add(new JLabel("Papyrus"));
        add(new JLabel("Glass"),config);
        add(new JLabel("Tab (Science)"),config);
        add(new JLabel("Comp. (Science)"),config);
        add(new JLabel("Gear (Science)"),config);
        add(new JLabel("Gold"),config);
        add(new JLabel("Victory"),config);
        add(new JLabel("Shields"),config);
        add(new JLabel("Stages"),config);
    }
}
