package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.SevenWonders;
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

    public void setCard(Card card) {
        this.card = card;
        update();
    }

    public Card getCard() {
        return this.card;
    }

    public void setCardWidth(int width) {
        this.width = width;
        setIcon(getIconOfSize(card, width));
    }

    public void setSelectionListener(CardSelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public void update() {
        setCardWidth(width);
    }

    private static Icon getIconOfSize(Card card, int width) {
        return new ImageIcon((new ImageIcon(card.getImagePath()))
                .getImage().getScaledInstance(width, -1, Image.SCALE_FAST));
    }

    private class CardMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if(selectionListener != null) {
                selectionListener.handleSelection(card);
            }
        }
    }
}
