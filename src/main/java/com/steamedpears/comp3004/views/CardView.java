package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.models.Card;

import javax.swing.*;
import java.awt.*;

public class CardView extends JLabel {
    public final int WIDTH = 300;
    private Card card;

    public CardView(Card card) {
        super(new ImageIcon(card.getImage().getScaledInstance(300,-1, Image.SCALE_FAST)));
        this.card = card;
    }
}
