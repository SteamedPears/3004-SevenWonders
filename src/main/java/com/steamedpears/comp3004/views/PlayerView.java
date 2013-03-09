package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.Player;

import com.steamedpears.comp3004.models.PlayerCommand;
import static com.steamedpears.comp3004.models.PlayerCommand.*;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class PlayerView extends JPanel {
    private static final int SELECTED_MULTIPLIER = 4;

    static Logger logger = Logger.getLogger(PlayerView.class);

    private Player player;
    private CardView selectedCardView;

    public PlayerView(Player player) {
        logger.info("Created with player[" + player + "]");
        setLayout(new MigLayout());
        this.player = player;
        update();
    }

    public void update() {
        removeAll();

        // cards in hand
        List<Card> hand = player.getHand();
        for(Card c : hand) {
            CardView cv = new CardView(c);
            cv.setSelectionListener(new CardSelectionListener(){
                @Override
                public void handleSelection(Card card) {
                    logger.info("Selecting " + card);
                    selectedCardView.setCard(card);
                }
            });
            add(cv, "aligny top");
        }

        // selected card
        if(hand.size() > 0) {
            selectedCardView = new CardView(hand.get(0), CardView.DEFAULT_WIDTH * SELECTED_MULTIPLIER);
            add(selectedCardView, "newline, span");
        }

        // discard button
        JButton discardButton = new JButton("Discard");
        discardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PlayerCommand move = new PlayerCommand();
                move.action = PlayerCardAction.DISCARD;
                move.card = selectedCardView.getCard().getId();
                player.setCurrentCommand(move);
                player.wake();
            }
        });
        add(discardButton,"span");

        // TODO: build button
        // TODO: use to build wonder button

        // assets
        add(new AssetHeaderView(),"gaptop 1, span, h 20!");
        add(new AssetView(this.player),"gaptop 1, span, h 20!");

        // TODO: add some way to switch to viewing another player
        // TODO: add some way to switch to high level view
    }
}
