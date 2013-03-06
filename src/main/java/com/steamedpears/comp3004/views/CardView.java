package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.models.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CardView extends JLabel {
    public static final int LIL_WIDTH = 200;
    public static final int BIG_WIDTH = 400;
    private Card card;

    public CardView(Card card) {
        super(getIconOfSize(card,BIG_WIDTH));
        addMouseListener(new MouseOverListener());
        this.card = card;
        shrink();
    }

    public void grow() {
        setIcon(getIconOfSize(card,BIG_WIDTH));
    }

    public void shrink() {
        setIcon(getIconOfSize(card,LIL_WIDTH));
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
    }
}
