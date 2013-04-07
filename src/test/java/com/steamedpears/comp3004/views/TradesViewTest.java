package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.assets.AssetMap;
import com.steamedpears.comp3004.models.players.Player;
import net.miginfocom.swing.MigLayout;
import org.junit.Ignore;

import javax.swing.*;

import static org.mockito.Mockito.*;

@Ignore
public class TradesViewTest extends JFrame {
    public static void main(String[] args) {
        new TradesViewTest();
    }

    public TradesViewTest() {
        setLayout(new MigLayout());

        // Mock players
        Player player = mock(Player.class);
        Player right = mock(Player.class);
        Player left = mock(Player.class);

        when(player.getPlayerRight()).thenReturn(right);
        when(player.getPlayerLeft()).thenReturn(left);
        when(left.getAssetsTradeable()).thenReturn(new AssetMap());
        when(right.getAssetsTradeable()).thenReturn(new AssetMap());

        // Set up the view
        add(new TradesView(player));
        pack();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }
}
