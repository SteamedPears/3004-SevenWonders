package com.steamedpears.comp3004;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.*;

public class ViewFrame extends JFrame {
    private JPanel panel;
    public SevenWonders controller;
    public NewGameDialog dialog;

    public void newGame() {
        dialog.setVisible(true);
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0,0,640,480);
        setResizable(false);
        setVisible(true);
        dialog = new NewGameDialog(this,new StartGameListener(),new CancelGameListener(),new NewGameDialogClose());
        newGame();
    }

    public ViewFrame(SevenWonders controller) {
        this.controller = controller;
        init();
    }

    private class StartGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            controller.startGame(dialog.isHostSelected(),dialog.getIpAddress());
        }
    }

    private class CancelGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            controller.exit();
        }
    }

    private class NewGameDialogClose extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent windowEvent) {
            controller.exit();
        }
    }
}
