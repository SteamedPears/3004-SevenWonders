package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.SevenWonders;

import javax.swing.*;
import java.awt.event.*;

public class JoinGameDialog extends GameDialog {

    private JLabel playersJoinedLabel;

    public JoinGameDialog(JFrame f, final SevenWonders controller) {
        super(f, "Host Game", controller);

        // initialize
        addWindowListener(new DialogClosingListener());

        // Players joined label
        add(new JLabel("Joining Game..."));
    }

    private class DialogClosingListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent windowEvent) {
            controller.openNewGameDialog();
        }
    }
}
