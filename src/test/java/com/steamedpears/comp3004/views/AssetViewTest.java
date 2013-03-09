package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Player;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class AssetViewTest extends JFrame {
    public AssetViewTest() {
        setBounds(0,0,960,560);

        // build player
        Player player = mock(Player.class);
        when(player.getAssets()).thenReturn(new HashMap<String, Integer>());

        setContentPane(new AssetView(player));

        setVisible(true);
    }

    public static void main(String[] args) {
        new AssetViewTest();
    }
}
