package com.steamedpears.comp3004;

import com.steamedpears.comp3004.routing.*;
import com.steamedpears.comp3004.views.*;

import javax.swing.*;

public class SevenWonders {
    public static final String PATH_RESOURCE = "src/main/resources/";
    public static final String PATH_DATA = PATH_RESOURCE + "data/";
    public static final String PATH_IMG = PATH_RESOURCE + "img/";
    public static final String PATH_IMG_CARDS = PATH_IMG + "cards/";
    public static final String PATH_IMG_WONDERS = PATH_IMG + "wonders/";
    public static final String PATH_CARDS = PATH_DATA + "cards.json";
    public static final String PATH_WONDERS = PATH_DATA + "wonderlist.json";

    public static final int MAX_PLAYERS = 7;
    public static final int MIN_PLAYERS = 3;

    public static void main(String[] args){
        new SevenWonders();
    }

    ViewFrame view;
    NewGameDialog newGameDialog;
    JDialog dialog;
    Router router;
    PlayerView playerView;
    HighLevelView highLevelView;

    public SevenWonders() {
        view = new ViewFrame(this);
        newGameDialog = new NewGameDialog(view,this);
        showNewGameDialog();
    }

    public void createGame(boolean isHost, String ipAddress, int port, int players) {
        hideNewGameDialog();
        if(isHost){
            router = Router.getHostRouter(port, players);
            dialog = new HostGameDialog(view,this);
        }else{
            router = Router.getClientRouter(ipAddress, port);
            dialog = new JoinGameDialog(view,this);
        }
        dialog.setVisible(true);
    }

    public void startGame() {
        router.beginGame();
        playerView = new PlayerView((router.getLocalGame()).getPlayerById(router.getLocalPlayerId()));
        highLevelView = new HighLevelView(this);
        view.setView(playerView);
    }

    public void showNewGameDialog() {
        if(dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
            dialog = null;
        }
        newGameDialog.setVisible(true);
    }

    public void hideNewGameDialog() {
        newGameDialog.setVisible(false);
    }

    public void exit() {
        System.exit(0);
    }
}
