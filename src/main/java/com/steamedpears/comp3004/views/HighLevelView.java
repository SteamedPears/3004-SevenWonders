package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.models.players.Player;

import javax.swing.*;
import java.util.*;

public class HighLevelView extends JPanel {
	private AssetView table;
	private List< Player > players;

    public HighLevelView(SevenWonders controller) {
        players = controller.getGame().getPlayers();
        table = new AssetView(players,controller.getLocalPlayer());
        add(table,"gapleft 0, gaptop 1, span 8");
    }
}
