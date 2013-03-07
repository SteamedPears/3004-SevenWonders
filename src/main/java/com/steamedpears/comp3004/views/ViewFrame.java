package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.SevenWonders;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;

public class ViewFrame extends JFrame {
    static Logger logger = Logger.getLogger(ViewFrame.class);

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0,0,800,600);
        setResizable(false);
        setVisible(true);
    }

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
