package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.models.Card;

import org.apache.log4j.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CardView extends JLabel {
    public static final int WIDTH_MULTIPLIER = 2;
    private int width = 200;
    private Card card;
    private SelectionListener selectionListener;
    static Logger logger = Logger.getLogger(SevenWonders.class);

    public CardView(Card card) {
        addMouseListener(new MouseOverListener());
        this.card = card;
        shrink();
    }

    public void grow() {
        setIcon(getIconOfSize(card, width * WIDTH_MULTIPLIER));
    }

    public void shrink() {
        setIcon(getIconOfSize(card, width));
    }

    public void setCardWidth(int width) {
        logger.debug("Setting width to: " + width);
        this.width = width;
        shrink();
    }

    public void setSelectionListener(SelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public abstract class SelectionListener {
        public abstract void handleSelection(Card card);
    }

    private static Icon getIconOfSize(Card card, int width) {
        return new ImageIcon(card.getImage().getScaledInstance(width, -1, Image.SCALE_FAST));
    }

    private class MouseOverListener extends MouseAdapter {
        public void mouseEntered(MouseEvent e) {
            grow();
        }

        public void mouseExited(MouseEvent e) {
            shrink();
        }

        public void mouseClicked(MouseEvent e) {
            if(selectionListener != null) {
                selectionListener.handleSelection(card);
            }
        }
    }
}
