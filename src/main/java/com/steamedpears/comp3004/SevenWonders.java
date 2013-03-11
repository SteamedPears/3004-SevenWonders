package com.steamedpears.comp3004;

import com.steamedpears.comp3004.routing.*;
import com.steamedpears.comp3004.views.*;
import org.apache.log4j.BasicConfigurator;

import org.apache.log4j.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;

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

    static Logger logger = Logger.getLogger(SevenWonders.class);

    public static void main(String[] args){
        //Logger.getRootLogger().setLevel(Level.INFO);
        BasicConfigurator.configure();
        new SevenWonders();
    }

    ViewFrame view;
    NewGameDialog newGameDialog;
    JDialog dialog;
    Router router;
    PlayerView playerView;
    boolean isHost;
    boolean gameStarted;

    public static enum View { PLAYER_VIEW, HIGHLEVEL_VIEW };
    private View currentView;

    // TODO: build this
    HighLevelView highLevelView;

    public SevenWonders() {
        view = new ViewFrame();
        openNewGameDialog();
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
        logger.info("Creating game");
        this.isHost = isHosting;
        closeNewGameDialog();
        if(isHost){
            router = Router.getHostRouter(port, players);
            dialog = new HostGameDialog(view,this);
        }else{
            router = Router.getClientRouter(ipAddress, port);
            dialog = new JoinGameDialog(view,this);
        }
        router.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                try {
                    if(isHost) {
                        if(router != null && dialog != null)
                            ((HostGameDialog)dialog).setPlayersJoined(router.getTotalHumanPlayers());
                    } else if(!gameStarted){
                        startGame();
                    }
                    updateView();
                } catch(Exception e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        });
        dialog.setVisible(true);
    }

    /**
     * Start the game, thus closing any open dialogs, opening the PlayerView.
     */
    public void startGame() {
        closeDialog();
        selectPlayerView();
        if(isHost) router.beginGame();
        this.gameStarted = true;
    }

    private void selectPlayerView() {
        currentView = View.PLAYER_VIEW;
    }

    private void updateView() throws Exception {
        if(currentView == null) return;
        switch(currentView) {
            case PLAYER_VIEW:
                playerView = new PlayerView((router.getLocalGame()).getPlayerById(router.getLocalPlayerId()));
                view.setView(playerView);
                break;
            case HIGHLEVEL_VIEW:
                // do stuff
                break;
            default:
                throw new Exception("Unknown view selected");
        }
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
        newGameDialog = new NewGameDialog(view,this);
        newGameDialog.setVisible(true);
    }

    /**
     * Close the new game dialog.
     */
    public void closeNewGameDialog() {
        if(newGameDialog == null) return;
        newGameDialog.setVisible(false);
        newGameDialog.dispose();
        newGameDialog = null;
    }

    /**
     * Close all windows and exit the game.
     */
    public void exit() {
        System.exit(0);
    }
}
