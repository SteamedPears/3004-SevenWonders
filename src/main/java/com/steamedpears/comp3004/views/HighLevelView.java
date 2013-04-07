package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.models.Player;

import javax.swing.*;
import java.util.*;

public class HighLevelView extends JPanel {
    private SevenWonders controller;
	private AssetView table;
	private List< Player > players;

    public HighLevelView(SevenWonders controller) {
        this.controller = controller;
        players = controller.getGame().getPlayers();
        table = new AssetView(players);
        add(table,"gapleft 0, gaptop 1, span 8");
    }
    public HighLevelView(SevenWonders controller, Player p) {
        this.controller = controller;
        players = controller.getGame().getPlayers();
        table = new AssetView(players,p);
        add(table,"gapleft 0, gaptop 1, span 8");

    }
}
