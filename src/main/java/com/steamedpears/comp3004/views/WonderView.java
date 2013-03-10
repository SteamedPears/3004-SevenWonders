package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Wonder;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WonderView extends JLabel {
    public static final int DEFAULT_WIDTH = 960;
    private Wonder wonder;
    private WonderSelectionListener selectionListener;
    static Logger logger = Logger.getLogger(WonderView.class);
    private int width;

    public WonderView(Wonder wonder) {
        this(wonder, DEFAULT_WIDTH);
    }

    public WonderView(Wonder wonder, int width) {
        this.width = width;
        addMouseListener(new WonderMouseListener());
        setWonder(wonder);
        update();
    }

    public void setWonder(Wonder wonder) {
        this.wonder = wonder;
        update();
    }

    public Wonder getWonder() {
        return this.wonder;
    }

    public void setCardWidth(int width) {
        this.width = width;
        setIcon(getIconOfSize(wonder, width));
    }

    public void setSelectionListener(WonderSelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public void update() {
        setCardWidth(width);
    }

    private static Icon getIconOfSize(Wonder wonder, int width) {
        return new ImageIcon((new ImageIcon(wonder.getImagePath()))
                .getImage().getScaledInstance(width, -1, Image.SCALE_SMOOTH));
    }

    private class WonderMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if(selectionListener != null) {
                selectionListener.handleSelection(wonder);
            }
        }
    }
}
