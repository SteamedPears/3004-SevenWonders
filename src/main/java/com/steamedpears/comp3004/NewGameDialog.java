package com.steamedpears.comp3004;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NewGameDialog extends JDialog {
    private final int width = 480;
    private final int height = 120;

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
    public String getipAddress() { return ipAddress; }

    public NewGameDialog(ViewFrame f,
                         ActionListener startButtonListener,
                         ActionListener cancelButtonListener,
                         WindowListener windowCloseListener) {
        super(f,"New Game",true);

        // initialization
        setLayout(new MigLayout());
        setBounds((f.getWidth() / 2) - (width / 2), (f.getHeight() / 2) - (height / 2), width, height);
        addWindowListener(windowCloseListener);
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
        cancelButton.addActionListener(cancelButtonListener);
        add(cancelButton);

        // Start game button
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(startButtonListener);
        add(startButton);
    }
}
