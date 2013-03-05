package com.steamedpears.comp3004.views;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NewGameDialog extends JDialog {
    private final int width = 480;
    private final int height = 120;

    private SevenWonders controller;

    private boolean isHost = true;
    private String ipAddress = "127.0.0.1";

    private JTextField addressField;

    private void selectHost() {
        isHost = true;
        hideAddressField();
    }

    private void selectClient() {
        isHost = false;
        showAddressField();
    }

    private void hideAddressField() {
        addressField.setVisible(false);
    }

    private void showAddressField() {
        addressField.setVisible(true);
    }

    public boolean isHostSelected() { return isHost; }
    public String getIpAddress() { return ipAddress; }

    public NewGameDialog(ViewFrame f, SevenWonders controller) {
        super(f,"New Game",true);

        // initialization
        this.controller = controller;
        setLayout(new MigLayout());
        setBounds((f.getWidth() / 2) - (width / 2), (f.getHeight() / 2) - (height / 2), width, height);
        addWindowListener(new DialogClosingListener());
        setResizable(false);

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
        addressField = new JTextField(ipAddress);
        addressField.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                ipAddress = addressField.getText();
            }
        });
        add(addressField, "span, grow");

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
            controller.startGame(isHostSelected(),getIpAddress());
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
