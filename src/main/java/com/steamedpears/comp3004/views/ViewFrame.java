package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.SevenWonders;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.*;

public class ViewFrame extends JFrame {
    private JPanel panel;
    public SevenWonders controller;

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0,0,640,480);
        setResizable(false);
        setVisible(true);
    }

    public void setView(JPanel view) {
        removeAll();
        add(view);
    }

    public ViewFrame(SevenWonders controller) {
        this.controller = controller;
        init();
    }
}
