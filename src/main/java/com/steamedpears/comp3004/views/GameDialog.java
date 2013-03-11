package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.SevenWonders;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class GameDialog extends JDialog {
    private static final int WIDTH = 480;
    private static final int HEIGHT = 120;

    protected SevenWonders controller;

    public GameDialog(JFrame f, String title, SevenWonders controller) {
        super(f,title,true);
        setLayout(new MigLayout("wrap 4"));
        int x = f.getX() + (f.getWidth() / 2) - (WIDTH / 2);
        int y = f.getY() + (f.getHeight() / 2) - (HEIGHT / 2);
        setBounds(x, y, WIDTH, HEIGHT);
        setResizable(false);

        this.controller = controller;
    }

    /**
     * Get the controller associated with this dialog.
     * @return The controller associated with this dialog.
     */
    public SevenWonders getController() { return controller; }
}
