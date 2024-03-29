package com.steamedpears.comp3004.views;
import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.models.*;

import static com.steamedpears.comp3004.models.PlayerCommand.*;

import com.steamedpears.comp3004.models.assets.AssetMap;
import com.steamedpears.comp3004.models.players.Player;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class PlayerView extends JPanel {
    private static final int SELECTED_MULTIPLIER = 3;

    static Logger logger = Logger.getLogger(PlayerView.class);

    public static final String AGE1_ICON = AssetView.PATH_ICON + "age1" + AssetView.IMAGE_TYPE_SUFFIX;
    public static final String AGE2_ICON = AssetView.PATH_ICON + "age2" + AssetView.IMAGE_TYPE_SUFFIX;
    public static final String AGE3_ICON = AssetView.PATH_ICON + "age3" + AssetView.IMAGE_TYPE_SUFFIX;

    private SevenWonders controller;
    private Player player;
    private CardView selectedCardView;
    private Map<String, String> persistentMessages;
    private int timer = 0;
    private boolean waiting;
    private boolean validPlayFree;
    private boolean validPlay;
    private boolean validBuild;
    private AssetMap leftTrades;
    private AssetMap rightTrades;

    private JLabel messageLabel;
    private JLabel persistentMessageLabel;
    private JButton discardButton;
    private JButton playFreeButton;
    private JButton playButton;
    private JButton buildButton;

    public PlayerView(SevenWonders controller) {
        logger.info("Created with player[" + player + "]");
        setLayout(new MigLayout("wrap 12, gap 0! 0!"));
        this.controller = controller;
        this.player = controller.getLocalPlayer();
        persistentMessages = new HashMap<String, String>();
        waiting = true;
        validPlayFree = false;
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
        PlayerCommand move = new PlayerCommand(
                PlayerCardAction.PLAY_FREE,
                selectedCardView.getCard().getId()
        );
        validPlayFree = player.isValid(move);
        move.action = PlayerCardAction.PLAY;
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

    /**
     * Remove a persistent message.
     * @param key the key of the message to be removed.
     */
    public void removeMessage(String key) {
        persistentMessages.remove(key);
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
        updateWithHand(player.getHand());
    }

    public void updateWithHand(final List<Card> hand) {
        removeAll();

        // selected card
        if(hand.size() > 0) {
            selectedCardView = new CardView(hand.get(0), CardView.DEFAULT_WIDTH * SELECTED_MULTIPLIER);
            add(selectedCardView,
                    "aligny top, newline, span " + SELECTED_MULTIPLIER + " " + SELECTED_MULTIPLIER);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    cardSelectionListener.handleSelection(hand.get(0));
                }
            });
        }

        // cards in hand
        for(Card c : hand) {
            CardView cv = new CardView(c);
            cv.setSelectionListener(cardSelectionListener);
            add(cv, "aligny top, alignx left");
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
                controller.setPlayerCommand(new PlayerCommand(
                        PlayerCardAction.DISCARD,
                        selectedCardView.getCard().getId()));
            }
        });
        buttonPanel.add(discardButton,"span");

        // play free button
        playFreeButton = new JButton("Play Free");
        playFreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doneMove();
                controller.setPlayerCommand(new PlayerCommand(
                        PlayerCardAction.PLAY_FREE,
                        selectedCardView.getCard().getId()));
            }
        });
        buttonPanel.add(playFreeButton,"span");

        // play button
        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doneMove();
                controller.setPlayerCommand(new PlayerCommand(
                        PlayerCardAction.PLAY,
                        selectedCardView.getCard().getId()));
            }
        });
        buttonPanel.add(playButton,"span");

        // build wonder button
        buildButton = new JButton("Build Wonder");
        buildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doneMove();
                controller.setPlayerCommand(new PlayerCommand(
                        PlayerCardAction.BUILD,
                        selectedCardView.getCard().getId()));
            }
        });
        if(player.hasFinishedWonder()) {
            buildButton.setEnabled(false);
        }
        buttonPanel.add(buildButton,"span");

        add(buttonPanel,"newline, span 3");

        // messages
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new MigLayout());

        messageLabel = new JLabel("The game has begun!");
        messagePanel.add(messageLabel, "span");

        persistentMessageLabel = new JLabel("");
        messagePanel.add(persistentMessageLabel,"span");

        add(messagePanel,"span");

        // assets
        add(new AssetView(this.player),"gapleft 0, gaptop 0, span 7");

        // age
        JLabel ageLabel = new JLabel();
        switch(player.getGame().getAge()) {
            case 1:
                logger.info("First age");
                ageLabel = AssetView.newJLabel(AGE1_ICON, 40);
                break;
            case 2:
                logger.info("Second age");
                ageLabel = AssetView.newJLabel(AGE2_ICON, 40);
                break;
            case 3:
                logger.info("Third age");
                ageLabel = AssetView.newJLabel(AGE3_ICON, 40);
                break;
            default:
                logger.error("Age was not 1,2, or 3...how?!");
                break;
        }
        add(ageLabel);

        // combo assets
        add(new ComboAssetView(player.getOptionalAssetsComplete()),"span, newline, gapleft 0, gaptop 1");

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
                setPlayFreeButtonEnabled(validPlayFree && !waiting && !timeLimitExpired);
                setPlayButtonEnabled(validPlay && !waiting && !timeLimitExpired);
                setBuildButtonEnabled(validBuild && !waiting && !timeLimitExpired);
            }
        });
    }

    public void setBuildButtonEnabled(final boolean enabled) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(buildButton == null)
                    return;
                buildButton.setEnabled(enabled);
            }
        });
    }

    public void setPlayFreeButtonEnabled(final boolean enabled) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(playFreeButton == null)
                    return;
                playFreeButton.setEnabled(enabled);
            }
        });
    }

    public void setPlayButtonEnabled(final boolean enabled) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(playButton == null)
                    return;
                playButton.setEnabled(enabled);
            }
        });
    }

    public void setDiscardButtonEnabled(final boolean enabled) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(discardButton == null)
                    return;
                discardButton.setEnabled(enabled);
            }
        });
    }

    /**
     * Updates the timer associated with this view.
     */
    public void updateTimer() {
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
