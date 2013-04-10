package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.SevenWonders;

import com.steamedpears.comp3004.models.players.Player;
import com.steamedpears.comp3004.models.players.strategies.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class HostGameDialog extends GameDialog {
    static Logger logger = Logger.getLogger(HostGameDialog.class);

    private JLabel playersJoinedLabel;
    private JComboBox aiComboBox;

    private Map<String,Strategy> nameToStrategy;
    

    public HostGameDialog(JFrame f, final SevenWonders controller) {
        super(f, "Host Game", controller);

        // initialize nameToStrategy
        nameToStrategy = new HashMap<String, Strategy>();
        nameToStrategy.put("Dumb", new NullStrategy());
        nameToStrategy.put("Random", new RandomStrategy());
        nameToStrategy.put("Heuristic", new HeuristicStrategy());
        nameToStrategy.put("Collusion", new CollusionStrategy());

        // initialize
        addWindowListener(new DialogClosingListener());

        // Players joined label
        add(new JLabel("Players joined:"));

        playersJoinedLabel = new JLabel("0");
        add(playersJoinedLabel);

        // Start game button
        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.startGame();
            }
        });
        add(startGameButton);

        // choose AI select
        aiComboBox = new JComboBox();
        aiComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Player.setDefaultStrategy((Strategy)aiComboBox.getSelectedItem());
            }
        });
        aiComboBox.addItem(new NullStrategy());
        aiComboBox.addItem(new RandomStrategy());
        aiComboBox.addItem(new HeuristicStrategy());
        aiComboBox.addItem(new CollusionStrategy());
        aiComboBox.setSelectedIndex(3);
        add(new JLabel("Strategy:"),"newline");
        add(aiComboBox);
    }

    /**
     * Update the count of players who have joined the game.
     * @param playersJoined The count of players who have joined the game.
     */
    public void setPlayersJoined(int playersJoined) {
        logger.info("Setting players joined to " + playersJoined);
        playersJoinedLabel.setText(""+playersJoined);
    }

    private class DialogClosingListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent windowEvent) {
            controller.openNewGameDialog();
        }
    }
}
