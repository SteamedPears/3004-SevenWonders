package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.Player;

import net.miginfocom.swing.MigLayout;
import org.apache.log4j.*;

import javax.swing.*;
import java.util.*;

public class PlayerView extends JPanel {
    static Logger logger = Logger.getLogger(PlayerView.class);

    private List<CardView> cardViews;

    private Player player;

    public PlayerView(Player player) {
        logger.info("Created with player[" + player + "]");
        setLayout(new MigLayout());
        this.player = player;
        update();
    }

    public void update() {
        removeAll();

        // add cards in hand
        cardViews = new ArrayList<CardView>();
        for(Card c : player.getHand()) {
            cardViews.add(new CardView(c));
        }
        for(CardView cv : cardViews) {
            add(cv, "aligny top");
        }

        // shield count labels
        add(new JLabel("Shields:"),"split 2,newline");
        add(new JLabel(getMilitaryWins(player).toString()));

        // gold count labels
        add(new JLabel("Gold:"),"split 2");
        add(new JLabel(""+player.getGold()));

        // TODO: add some way to switch to viewing another player
        // TODO: add some way to switch to high level view
        // TODO: add tabular view of resources
        // TODO: add tabular view of sciences
        // TODO: add victory point count
    }

    // WHY DO I HAVE TO DO THIS?!  ARRGGHHH
    private static Integer getMilitaryWins(Player p) {
        int wins = 0;
        for(int i : p.getMilitaryResults()) {
            if(i > 0) {
                wins += i;
            }
        }
        return wins;
    }
}
