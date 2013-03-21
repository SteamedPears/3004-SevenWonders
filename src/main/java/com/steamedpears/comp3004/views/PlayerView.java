package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.models.*;

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

    public static final String AGE1_ICON = AssetView.PATH_ICON + "age1" + AssetView.IMAGE_TYPE_SUFFIX;
    public static final String AGE2_ICON = AssetView.PATH_ICON + "age2" + AssetView.IMAGE_TYPE_SUFFIX;
    public static final String AGE3_ICON = AssetView.PATH_ICON + "age3" + AssetView.IMAGE_TYPE_SUFFIX;

    private Player player;
    private CardView selectedCardView;
    private Map<String, String> persistentMessages;
    private int timer = 0;
    private boolean waiting;
    private boolean validPlay;
    private boolean validBuild;
    private AssetMap leftTrades;
    private AssetMap rightTrades;

    private JLabel messageLabel;
    private JLabel persistentMessageLabel;
    private JButton discardButton;
    private JButton playButton;
    private JButton buildButton;

    public PlayerView(Player player) {
        logger.info("Created with player[" + player + "]");
        setLayout(new MigLayout());
        this.player = player;
        persistentMessages = new HashMap<String, String>();
        waiting = true;
        validPlay = false;
        validBuild = false;
        update();
    }

    private CardSelectionListener cardSelectionListener = new CardSelectionListener(){
        @Override
        public void handleSelection(Card card) {
            logger.info("Selecting " + card);
            if(selectedCardView != null) {
                selectedCardView.setCard(card);
            }
            validateMoves();
        }
    };

    /**
     * Validates play and build player actions, including left and right trades
     */
    public void validateMoves() {
        PlayerCommand move = new PlayerCommand(PlayerCardAction.PLAY,selectedCardView.getCard().getId());
        if(leftTrades != null) move.leftPurchases = leftTrades;
        if(rightTrades != null) move.rightPurchases = rightTrades;
        validPlay = player.isValid(move);
        move.action = PlayerCardAction.BUILD;
        validBuild = player.isValid(move);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateButtonState();
            }
        });

    }

    /**
     * Set the left and right trades to be performed when making a player action
     * @param leftTrades the trades with the player's left neighbor
     * @param rightTrades the trades with the player's right neighbor
     */
    public void setTrades(AssetMap leftTrades, AssetMap rightTrades) {
        this.leftTrades = leftTrades;
        this.rightTrades = rightTrades;
        validateMoves();
    }

    /**
     * Set the ephemeral message on this view.
     * @param message The ephemeral message to be displayed.
     */
    public void setMessage(String message) {
        if(messageLabel == null) return;
        messageLabel.setText(message);
    }

    /**
     * Add a persistent message to this view.
     * @param key The key to which to assign this message (for easy removal).
     * @param message The persistent message to be displayed.
     */
    public void addMessage(String key, String message) {
        persistentMessages.put(key,message);
        updatePersistentMessages();
    }

    private void updatePersistentMessages() {
        StringBuffer message = new StringBuffer();
        message.append("<html>");
        for(String s : persistentMessages.values()) {
            message.append(s);
            message.append("<br>");
        }
        message.append("</html>");
        persistentMessageLabel.setText(message.toString());
    }

    /**
     * Update the view.
     */
    public void update() {
        removeAll();

        // cards in hand
        final List<Card> hand = player.getHand();
        for(Card c : hand) {
            CardView cv = new CardView(c);
            cv.setSelectionListener(cardSelectionListener);
            add(cv, "aligny top");
        }

        // selected card
        if(hand.size() > 0) {
            selectedCardView = new CardView(hand.get(0), CardView.DEFAULT_WIDTH * SELECTED_MULTIPLIER);
            add(selectedCardView, "newline, span " + SELECTED_MULTIPLIER);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    cardSelectionListener.handleSelection(hand.get(0));
                }
            });
        }

        // group action buttons together
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new MigLayout());

        // discard button
        discardButton = new JButton("Discard");
        discardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doneMove();
                player.setCurrentCommand(new PlayerCommand(
                        PlayerCardAction.DISCARD,
                        selectedCardView.getCard().getId()));
                player.wake();
            }
        });
        buttonPanel.add(discardButton,"span");

        // play button
        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doneMove();
                PlayerCommand move = new PlayerCommand(
                        PlayerCardAction.PLAY,
                        selectedCardView.getCard().getId());
                if(leftTrades != null) move.leftPurchases = leftTrades;
                if(rightTrades != null) move.rightPurchases = rightTrades;
                player.setCurrentCommand(move);
                player.wake();
            }
        });
        buttonPanel.add(playButton,"span");

        // build wonder button
        buildButton = new JButton("Build Wonder");
        buildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doneMove();
                PlayerCommand move = new PlayerCommand(
                        PlayerCardAction.BUILD,
                        selectedCardView.getCard().getId());
                if(leftTrades != null) move.leftPurchases = leftTrades;
                if(rightTrades != null) move.rightPurchases = rightTrades;
                player.setCurrentCommand(move);
                player.wake();
            }
        });
        if(player.hasFinishedWonder()) {
            buildButton.setEnabled(false);
        }
        buttonPanel.add(buildButton,"span");

        add(buttonPanel,"span 2");

        // messages
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new MigLayout());

        messageLabel = new JLabel("The game has begun!");
        messagePanel.add(messageLabel, "span");

        persistentMessageLabel = new JLabel("");
        messagePanel.add(persistentMessageLabel,"span");

        add(messagePanel,"span");

        // assets
        add(new AssetView(this.player),"gapleft 0, gaptop 1, span 8");

        // age
        JLabel ageLabel = new JLabel();
        switch(player.getGame().getAge()) {
            case 1:
                logger.info("First age");
                ageLabel = AssetView.newJLabel(AGE1_ICON);
                break;
            case 2:
                logger.info("Second age");
                ageLabel = AssetView.newJLabel(AGE2_ICON);
                break;
            case 3:
                logger.info("Third age");
                ageLabel = AssetView.newJLabel(AGE3_ICON);
                break;
            default:
                logger.error("Age was not 1,2, or 3...how?!");
                break;
        }
        add(ageLabel);

        waitForTurn();
    }

    /**
     * Signal that the UI is no longer waiting for the protocol to allow player actions
     */
    public void newMove() {
        waiting = false;
        updateButtonState();
        timer = SevenWondersGame.TURN_LENGTH / 1000;
        updateTimer();
    }

    /**
     * Signal that the UI should wait for the protocol to allow player actions
     */
    public void waitForTurn() {
        waiting = true;
        updateButtonState();
        timer = 0;
        updateTimer();
    }

    /**
     * Signal that the player has chosen a move and is now waiting for the turn to end
     */
    public void doneMove() {
        waiting = false;
        updateButtonState();
        timer = 0;
        updateTimer();
    }

    public void updateButtonState() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boolean timeLimitExpired = timer <= 0;
                setDiscardButtonEnabled(!waiting && !timeLimitExpired);
                setPlayButtonEnabled(validPlay && !waiting && !timeLimitExpired);
                setBuildButtonEnabled(validBuild && !waiting && !timeLimitExpired);
            }
        });
    }

    public void setBuildButtonEnabled(final boolean enabled) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                buildButton.setEnabled(enabled);
            }
        });
    }

    public void setPlayButtonEnabled(final boolean enabled) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                playButton.setEnabled(enabled);
            }
        });
    }

    public void setDiscardButtonEnabled(final boolean enabled) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                discardButton.setEnabled(enabled);
            }
        });
    }

    /**
     * Updates the timer associated with this view.
     */
    public void updateTimer() {
        logger.info("Updating timer");
        if(waiting) {
            setMessage("Waiting...");
        } else if(timer <= 0) {
            setMessage("Move chosen.");
        } else {
            int minutes = timer / 60;
            int seconds = timer % 60;
            setMessage(minutes + ":" + String.format("%02d",seconds) + " left to choose a move.");
            --timer;
        }
    }
}
