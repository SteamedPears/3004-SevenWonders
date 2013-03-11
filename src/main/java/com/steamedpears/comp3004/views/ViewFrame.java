package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.SevenWonders;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;

public class ViewFrame extends JFrame {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 560;

    static Logger logger = Logger.getLogger(ViewFrame.class);

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0,0,WIDTH,HEIGHT);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Set the view to be displayed in this frame.
     * @param view The view to be displayed in this frame.
     */
    public void setView(JPanel view) {
        logger.info("Setting view to " + view);
        getContentPane().removeAll();
        getContentPane().add(view);
        validate();
        repaint();
    }

    public ViewFrame() {
        init();
    }
}
