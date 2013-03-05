package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.*;
import com.steamedpears.comp3004.routing.*;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NewGameDialog extends JDialog {
    private SevenWonders controller;

    // Configuration
    private final int width = 480;
    private final int height = 120;
    private boolean isHost = true;
    private String ipAddress = "127.0.0.1";
    private Integer port = Router.HOST_PORT;

    // Interface elements
    private JLabel addressLabel;
    private JTextField addressField;
    private JTextField portField;

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
        addressLabel.setVisible(false);
    }

    private void showAddressField() {
        addressField.setVisible(true);
        addressLabel.setVisible(true);
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
        addressLabel = new JLabel("IP:");
        add(addressLabel);
        addressField = new JTextField(ipAddress);
        addressField.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                ipAddress = addressField.getText();
            }
        });
        add(addressField,"span");

        // port field
        add(new JLabel("Port:"));
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
        add(portField);

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
