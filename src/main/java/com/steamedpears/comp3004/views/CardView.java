package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.models.Card;

import org.apache.log4j.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CardView extends JLabel {
    public static final int DEFAULT_WIDTH = 100;
    private Card card;
    private CardSelectionListener selectionListener;
    static Logger logger = Logger.getLogger(CardView.class);
    private int width;

    public CardView(Card card) {
        this(card, DEFAULT_WIDTH);
    }

    public CardView(Card card, int width) {
        this.width = width;
        addMouseListener(new CardMouseListener());
        setCard(card);
        update();
    }

    /**
     * Set the cardID associated with this view, and update.
     * @param card The cardID to be associated with this view.
     */
    public void setCard(Card card) {
        this.card = card;
        update();
    }

    /**
     * Get the cardID associated with this view.
     * @return The cardID associated with this view.
     */
    public Card getCard() {
        return this.card;
    }

    /**
     * Set the width of the displayed cardID.
     * @param width The desired width.
     */
    public void setCardWidth(int width) {
        this.width = width;
        setIcon(getIconOfSize(card, width));
    }

    /**
     * Set the selection listener to be associated with selecting a cardID in this view.
     * @param selectionListener The selection listener to be associated with selecting a cardID.
     */
    public void setSelectionListener(CardSelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    /**
     * Update the view.
     */
    public void update() {
        setCardWidth(width);
    }

    private static Icon getIconOfSize(Card card, int width) {
        return new ImageIcon((new ImageIcon(card.getImagePath()))
                .getImage().getScaledInstance(width, -1, Image.SCALE_SMOOTH));
    }

    private class CardMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if(selectionListener != null) {
                selectionListener.handleSelection(card);
            }
        }
    }
}
