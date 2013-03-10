package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.*;
import com.steamedpears.comp3004.routing.*;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NewGameDialog extends GameDialog {

    // Configuration
    private boolean isHost = true;
    private String ipAddress = "127.0.0.1";
    private Integer port = Router.HOST_PORT;
    private int players = 3;

    // Interface elements
    private JLabel addressLabel;
    private JTextField addressField;
    private JTextField portField;
    private JLabel playersLabel;
    private JComboBox playersComboBox;

    private void selectHost() {
        isHost = true;
        hideAddressField();
        showPlayerSelect();
    }

    private void selectClient() {
        isHost = false;
        showAddressField();
        hidePlayerSelect();
    }

    private void hideAddressField() {
        addressField.setVisible(false);
        addressLabel.setVisible(false);
    }

    private void showAddressField() {
        addressField.setVisible(true);
        addressLabel.setVisible(true);
    }

    private void hidePlayerSelect() {
        playersLabel.setVisible(false);
        playersComboBox.setVisible(false);
    }

    private void showPlayerSelect() {
        playersLabel.setVisible(true);
        playersComboBox.setVisible(true);
    }

    public boolean isHostSelected() { return isHost; }
    public String getIpAddress() { return ipAddress; }
    public int getPort() { return port; }
    public int getPlayers() { return players; }

    public NewGameDialog(JFrame f, SevenWonders controller) {
        super(f,"New Game",controller);

        // initialization
        this.controller = controller;
        addWindowListener(new DialogClosingListener());

        // host/client buttons
        ButtonGroup hostClientButtonGroup = new ButtonGroup();

        JRadioButton hostButton = new JRadioButton("Host");
        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectHost();
            }
        });
        hostClientButtonGroup.add(hostButton);
        add(hostButton);

        JRadioButton clientButton = new JRadioButton("Client");
        clientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectClient();
            }
        });
        hostClientButtonGroup.add(clientButton);
        add(clientButton);

        // address field
        addressLabel = new JLabel("IP:");
        add(addressLabel,"split 2");
        addressField = new JTextField(ipAddress);
        addressField.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                ipAddress = addressField.getText();
            }
        });
        add(addressField);

        // port field
        add(new JLabel("Port:"),"split 2");
        portField = new JTextField(port.toString());
        portField.addPropertyChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                String portText = portField.getText();
                if(portText.equals(""))
                    return;
                port = Integer.parseInt(portField.getText(),10);
            }
        });
        add(portField,"grow");

        // players dropdown
        playersLabel = new JLabel("Players:");
        add(playersLabel);
        playersComboBox = new JComboBox();
        for(int i = SevenWonders.MIN_PLAYERS; i <= SevenWonders.MAX_PLAYERS; ++i) {
            playersComboBox.addItem(new Integer(i));
        }
        playersComboBox.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                players = (Integer)playersComboBox.getSelectedItem();
            }
        });
        add(playersComboBox);

        // by default, offer to host
        hostButton.setSelected(true);
        selectHost();

        // Cancel game button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelGameListener());
        add(cancelButton);

        // Start game button
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(new StartGameListener());
        add(startButton);
    }

    private class StartGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            controller.createGame(isHostSelected(), getIpAddress(), getPort(), getPlayers());
        }
    }

    private class CancelGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            controller.exit();
        }
    }

    private class DialogClosingListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent windowEvent) {
            controller.exit();
        }
    }
}
