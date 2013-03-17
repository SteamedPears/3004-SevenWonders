package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.AssetMap;
import com.steamedpears.comp3004.models.Player;
import net.miginfocom.swing.MigLayout;
import org.junit.Ignore;

import javax.swing.*;

import static org.mockito.Mockito.*;

@Ignore
public class PlayerTradesViewTest extends JFrame {
    public static void main(String[] args) {
        new PlayerTradesViewTest();
    }

    public PlayerTradesViewTest() {
        setLayout(new MigLayout());

        // Mock players
        Player player = mock(Player.class);
        Player left = mock(Player.class);

        when(player.getPlayerLeft()).thenReturn(left);
        when(left.getAssetsTradeable()).thenReturn(new AssetMap());

        // Set up the view
        add(new PlayerTradesView(player,left));
        pack();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }
}
