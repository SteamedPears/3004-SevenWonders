package com.steamedpears.comp3004.views;

import javax.swing.*;

public class AssetHeaderViewTest extends JFrame {
    public AssetHeaderViewTest() {
        setBounds(0,0,960,560);
        add(new AssetHeaderView());
        setVisible(true);
    }

    public static void main(String[] args) {
        new AssetHeaderViewTest();
    }
}
