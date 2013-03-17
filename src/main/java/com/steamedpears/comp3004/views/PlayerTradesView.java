package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import javax.swing.*;

public class PlayerTradesView extends JPanel {
    private static Logger log = Logger.getLogger(Player.class);

    private Player player;
    private Player neighbor;
    private boolean isLeft;
    private PlayerCommand command;

    public PlayerTradesView(Player player, Player neighbor) {
        // set up instance variables
        this.player = player;
        this.neighbor = neighbor;
        this.isLeft = this.player.getPlayerLeft().equals(neighbor);
        this.command = new PlayerCommand();

        // set up view
        setLayout(new MigLayout("insets 0, gap 0"));
        update();
    }

    public void update() {
        removeAll();

        // display assets
        add(new AssetView(neighbor.getAssetsTradeable(),neighbor.getOptionalAssetsCompleteTradeable()));
    }

    /**
     * Get the command currently being displayed by this view
     * @return the command currently being displayed
     */
    public PlayerCommand getCommand() { return command; }
}
