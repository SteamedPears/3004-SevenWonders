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
    JPanel panel;

    public void newGame() {
        new NewGameDialog(this,new StartGameListener(),new CancelGameListener(),new NewGameDialogClose());
    }

    private void startGame() {
        // TODO: make this do something
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0,0,640,480);
        setResizable(false);
        setVisible(true);
        newGame();
    }

    public ViewFrame() {
        init();
    }

    private class StartGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // TODO: do something here
        }
    }

    private class CancelGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.exit(0);
        }
    }

    private class NewGameDialogClose extends WindowAdapter {
        @Override
        public void windowClosed(WindowEvent windowEvent) {
            System.exit(0);
        }
    }
}
