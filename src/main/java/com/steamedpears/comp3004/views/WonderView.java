package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Wonder;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WonderView extends JLabel {
    public static final int DEFAULT_WIDTH = 920;
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

    /**
     * Set the wonder associated with this view, and update.
     * @param wonder The wonder to be associated with this view.
     */
    public void setWonder(Wonder wonder) {
        this.wonder = wonder;
        update();
    }

    /**
     * Get the wonder associated with this view.
     * @return The wonder associated with this view.
     */
    public Wonder getWonder() {
        return this.wonder;
    }

    /**
     * Set the width of the wonder image being displayed.
     * @param width The desired width (in pixels) of the wonder image.
     */
    public void setWonderWidth(int width) {
        this.width = width;
        setIcon(getIconOfSize(wonder, width));
    }

    /**
     * Set the selection listener to respond to the selection of a wonder.
     * @param selectionListener The selection listener to respond to the selection of a wonder.
     */
    public void setSelectionListener(WonderSelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    /**
     * Update the view.
     */
    public void update() {
        setWonderWidth(width);
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
