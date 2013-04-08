package com.steamedpears.comp3004.views;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class ViewFrame extends JFrame {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 560;

    static Logger logger = Logger.getLogger(ViewFrame.class);

    JTabbedPane pane;

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);
        UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0,0,0,0));
        UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);
        setResizable(false);
        pane = new JTabbedPane();
        pane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        setContentPane(pane);
    }

    /**
     * Set the view to be displayed in this frame.
     * @param view The view to be displayed in this frame.
     * @param title the title of the frame
     */
    public void addTab(JPanel view, String title) {
        view.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        synchronized (pane) {
            if(hasTab(title)) {
                pane.setComponentAt(pane.indexOfTab(title),view);
            } else {
                pane.addTab(title, view);
            }
        }
    }

    public void hideTabs() {
        pane.setVisible(false);
    }

    public void showTabs() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                pane.setVisible(true);
            }
        });
    }

    /**
     * Removes all tabs.
     */
    public void clearTabs() {
        pane.removeAll();
    }

    private boolean hasTab(String title) {
        return pane.indexOfTab(title) != -1;
    }

    private void removeTab(String title) {
        pane.removeTabAt(pane.indexOfTab(title));
    }

    public ViewFrame() {
        super("Seven Wonders by Team 4");
        init();
    }
}
