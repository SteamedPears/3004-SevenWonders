package com.steamedpears.comp3004.views;

import net.miginfocom.swing.MigLayout;
import org.apache.log4j.*;
import org.junit.Ignore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Ignore
public class ViewFrameTest {
    private ViewFrame view;

    public ViewFrameTest() {
        // create the window
        view = new ViewFrame();

        // create test panels
        final JPanel firstPanel = new JPanel(){
            public String toString() {
                return "firstPanel";
            }
        };
        firstPanel.add(new JLabel("ViewFrame properly displays first dynamic panel"));
        final JPanel secondPanel = new JPanel(){
            public String toString() {
                return "secondPanel";
            }
        };
        secondPanel.add(new JLabel("ViewFrame properly displays second dynamic panel"));

        // create a control dialog
        JDialog controls = new JDialog(view,"Controls",false);
        controls.setLayout(new MigLayout());
        controls.setResizable(false);

        JButton firstButton = new JButton("One");
        firstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                view.addTab(firstPanel, "Hand");
            }
        });
        controls.add(firstButton);

        JButton secondButton = new JButton("Two");
        secondButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                view.addTab(secondPanel, "Hand");
            }
        });
        controls.add(secondButton);

        controls.pack();
        controls.setVisible(true);

        // add a test panel
        JPanel labelPanel = new JPanel();
        labelPanel.add(new JLabel("ViewFrame properly displays initial panel"));
        view.addTab(labelPanel, "Hand");
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        new ViewFrameTest();
    }
}
