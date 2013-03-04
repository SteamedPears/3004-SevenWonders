package com.steamedpears.comp3004;

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

    public ViewFrame(SevenWonders controller) {
        this.controller = controller;
        init();
    }
}
