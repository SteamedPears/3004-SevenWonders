package com.steamedpears.comp3004;

import com.steamedpears.comp3004.models.*;
import com.steamedpears.comp3004.models.assets.AssetMap;
import com.steamedpears.comp3004.models.players.Player;
import com.steamedpears.comp3004.routing.*;
import com.steamedpears.comp3004.views.*;
import org.apache.log4j.*;

import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class SevenWonders {

    public static final String PATH_RESOURCE = File.separator;
    public static final String PATH_DATA = PATH_RESOURCE + "data"+ File.separator;
    public static final String PATH_IMG = PATH_RESOURCE + "img"+File.separator;
    public static final String PATH_IMG_CARDS = PATH_IMG + "cards"+File.separator;
    public static final String PATH_IMG_WONDERS = PATH_IMG + "wonders"+File.separator;
    public static final String PATH_CARDS = PATH_DATA + "cards.json";
    public static final String PATH_WONDERS = PATH_DATA + "wonderlist.json";
    public static final String IMAGE_TYPE_SUFFIX = ".jpg";

    public static final int MAX_PLAYERS = 7;
    public static final int MIN_PLAYERS = 3;

    private static Logger logger = Logger.getLogger(SevenWonders.class);

    public static void main(String[] args){
        if(args.length > 0) {
            pickedWonder = args[0];
        }
        if(args.length > 1) {
            pickedSide = args[1];
        }
        BasicConfigurator.configure();
        new SevenWonders();
    }

    private Router router;
    private boolean isHost;
    private boolean gameStarted;
    private boolean playedLastCard = false;
    private java.util.Timer scheduledUpdateTimer;
    private PlayerCommand playerCommand;
    private PlayerCommand tradesCommand;
    private PlayerCommand undiscardCommand;
    private PlayerCommand playExtraCommand;

    private ViewFrame view;
    private NewGameDialog newGameDialog;
    private JDialog dialog;
    private PlayerView playerView;
    private TradesView tradesView;
    private HighLevelView highLevelView;
    private DiscardView discardView;

    private static String pickedWonder = null;
    private static String pickedSide = null;

    public SevenWonders() {
        view = new ViewFrame();
        scheduledUpdateTimer = new java.util.Timer();
        scheduledUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(playerView != null) {
                    playerView.updateTimer();
                }
            }
        },new Date(),1000);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.setVisible(true);
                openNewGameDialog();
            }
        });
    }

    /**
     * Create a game with the given parameters.
     * @param isHosting True if this computer will host the game.
     * @param ipAddress If this computer is NOT hosting the game, the IP of the host.
     * @param port The TCP port on which to accept connections (if this computer is hosting the game),
     *             or to which to connect (if this computer is NOT hosting the game).
     * @param players The desired number of players, if this computer is hosting the game.
     */
    public void createGame(boolean isHosting, String ipAddress, int port, int players) {
        logger.info("Creating game with " + players + " players");
        this.isHost = isHosting;
        closeNewGameDialog();
        if(isHosting){
            router = Router.getHostRouter(port, players);
            if(pickedWonder != null) {
                router.setLocalWonder(pickedWonder);
            }
            if(pickedSide != null) {
                router.setLocalSide(pickedSide);
            }
            dialog = new HostGameDialog(view,this);
        }else{
            router = Router.getClientRouter(ipAddress, port);
            dialog = new JoinGameDialog(view,this);
        }
        router.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if (changeEvent.getSource() instanceof SevenWondersGame) {
                    handleGameChange();
                } else if (changeEvent.getSource() instanceof Player){
                    handlePlayerChange();
                } else if (changeEvent.getSource() instanceof Router){
                    handleRouterChange();
                } else {
                    logger.error("Unknown change source!");
                }
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialog.setVisible(true);
            }
        });
    }

    private void handleGameChange() {
        logger.info("Handling game change");
        if(!gameStarted) {
            gameStarted = true;
            startGame();
        } else if(getGame().isGameOver()) {
            view.clearTabs();
            playerView = null;
            highLevelView = null;
            dialog = new ResultsDialog(view,this);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    dialog.setVisible(true);
                }
            });
        } else {
            for(Player p : getGame().getPlayers()) {
                logger.info(p + " has " + p.getFinalVictoryPoints() + " victory points");
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateView();
                }
            });
        }
    }

    private void handlePlayerChange() {
        logger.info("Handling player change");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                playerView.newMove();
            }
        });
    }

    private void handleRouterChange() {
        logger.info("Handling router change");
        if(router != null && !router.isValid()) {
            router = null;
            openNewGameDialog();
        } else if(dialog != null) {
            ((HostGameDialog)dialog).setPlayersJoined(router.getTotalHumanPlayers());
        }
    }

    /**
     * Start the game, thus closing any open dialogs, opening the PlayerView.
     */
    public void startGame() {
        closeDialog();
        if(isHost) router.beginGame();
        this.gameStarted = true;
        updateView();
    }

    private void updateView() {
        Player thisPlayer = getLocalPlayer();
        playerView = new PlayerView(this);
        view.addTab(playerView, "Hand");
        tradesView = new TradesView(thisPlayer);
        tradesView.setListener(new TradesView.TradeChangeListener() {
            @Override
            public void handleChange(AssetMap leftTrades, AssetMap rightTrades) {
                playerView.setTrades(leftTrades, rightTrades);
                PlayerCommand tradesCommand = new PlayerCommand();
                tradesCommand.leftPurchases = leftTrades;
                tradesCommand.rightPurchases = rightTrades;
                setTradesCommand(tradesCommand);
            }
        });
        view.addTab(tradesView,"Trades");
        discardView = new DiscardView(this);
        view.addTab(discardView,"Discard Pile");
        highLevelView = new HighLevelView(this);
        view.addTab(highLevelView,"Table");
        for(Player player : getGame().getPlayers()) {
            String title = "Player " + player.getPlayerId();
            if(player.equals(thisPlayer)) {
                title = "You";
            } else if(player.equals(thisPlayer.getPlayerLeft())) {
                title = "Left";
            } else if(player.equals(thisPlayer.getPlayerRight())) {
                title = "Right";
            }
            view.addTab(new StructuresView(player),title);
        }
    }

    /**
     * Gets the game associated with this View.
     */
    public SevenWondersGame getGame() {
        return router.getLocalGame();
    }

    public Player getLocalPlayer() {
        return getGame().getPlayerById(router.getLocalPlayerId());
    }

    /**
     * Close any open dialogs.
     */
    public void closeDialog() {
        if(dialog == null) return;
        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;
    }

    /**
     * Open the new game dialog.
     */
    public void openNewGameDialog() {
        closeDialog();
        if(router != null) {
            router.cleanup();
        }
        router = null;
        newGameDialog = new NewGameDialog(view,this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                newGameDialog.setVisible(true);
            }
        });
    }

    /**
     * Close the new game dialog.
     */
    public void closeNewGameDialog() {
        if(newGameDialog == null) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                newGameDialog.setVisible(false);
                newGameDialog.dispose();
                newGameDialog = null;
            }
        });
    }

    /**
     * Close all windows and exit the game.
     */
    public void exit() {
        System.exit(0);
    }

    public void setPlayerCommand(PlayerCommand command) {
        if(getLocalPlayer().canPlayLastCard() && !playedLastCard && getGame().isLastTurnInAge()) {
            setPlayExtraCommand(command);
            playedLastCard = true;
            // make player view look like the card was played
            List<Card> newHand = new ArrayList<Card>();
            for(Card c : getLocalPlayer().getHand()) {
                if(c.getId().equals(command.cardID))
                    continue;
                newHand.add(c);
            }
            playerView.updateWithHand(newHand);
            playerView.addMessage("lastCard","You can play your last card!");
            playerView.newMove();
        } else {
            playedLastCard = false;
            playerCommand = command;
            playerView.removeMessage("lastCard");
            doneMove();
        }
    }

    public void setTradesCommand(PlayerCommand command) {
        tradesCommand = command;
    }

    public void setUndiscardCommand(PlayerCommand command) {
        undiscardCommand = command;
    }

    public void setPlayExtraCommand(PlayerCommand command) {
        playExtraCommand = command;
    }

    public void doneMove() {
        Player thisPlayer = getLocalPlayer();
        PlayerCommand command = PlayerCommand.getNullCommand(thisPlayer);
        PlayerCommand firstCommand = command;
        if(playerCommand != null) {
            command.action = playerCommand.action;
            command.cardID = playerCommand.cardID;
            playerCommand = null;
        }
        if(tradesCommand != null) {
            command.rightPurchases = tradesCommand.rightPurchases;
            command.leftPurchases = tradesCommand.leftPurchases;
            tradesCommand = null;
        }
        if(undiscardCommand != null) {
            command.followup = undiscardCommand;
            command = undiscardCommand;
            undiscardCommand = null;
        }
        if(playExtraCommand != null) {
            command.followup = playExtraCommand;
            command = playExtraCommand;
            playExtraCommand = null;
        }
        thisPlayer.setCurrentCommand(firstCommand);
        thisPlayer.wake();
    }
}
