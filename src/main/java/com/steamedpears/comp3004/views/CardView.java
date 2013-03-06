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
    private SelectionListener selectionListener;
    static Logger logger = Logger.getLogger(SevenWonders.class);

    public CardView(Card card) {
        this(card, DEFAULT_WIDTH);
    }

    public CardView(Card card, int width) {
        addMouseListener(new CardMouseListener());
        setCard(card);
        setCardWidth(width);
    }

    public void setCard(Card card) { this.card = card; }

    public void setCardWidth(int width) {
        setIcon(getIconOfSize(card, width));
    }

    public void setSelectionListener(SelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public abstract class SelectionListener {
        public abstract void handleSelection(Card card);
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
