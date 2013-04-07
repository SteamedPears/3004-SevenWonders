package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.assets.AssetMap;
import com.steamedpears.comp3004.models.players.Player;
import org.junit.Ignore;

import javax.swing.*;

import static org.mockito.Mockito.*;

@Ignore
public class AssetViewTest extends JFrame {
    public AssetViewTest() {
        setBounds(0,0,960,560);

        // build player
        Player player = mock(Player.class);
        when(player.getAssets()).thenReturn(new AssetMap());

        setContentPane(new AssetView(player));

        setVisible(true);
    }

    public static void main(String[] args) {
        new AssetViewTest();
    }
}
