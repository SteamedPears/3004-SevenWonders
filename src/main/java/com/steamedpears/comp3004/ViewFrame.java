package com.steamedpears.comp3004;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.*;

/**
 * Created with IntelliJ IDEA.
 * User: spratt
 * Date: 2013-03-02
 * Time: 7:40 PM
 * To change this template use File | Settings | File Templates.
 */
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
            controller.startGame(dialog.isHostSelected(),dialog.getipAddress());
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
