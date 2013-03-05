package com.steamedpears.comp3004;

import com.steamedpears.comp3004.models.*;
import com.steamedpears.comp3004.routing.*;
import com.steamedpears.comp3004.views.*;

import java.util.ArrayList;
import java.util.List;

public class SevenWonders {
    public static final String PATH_RESOURCE = "src/main/resources/";
    public static final String PATH_DATA = PATH_RESOURCE + "data/";
    public static final String PATH_IMG = PATH_RESOURCE + "img/";
    public static final String PATH_CARDS = PATH_DATA + "cards.json";
    public static final String PATH_WONDERS = PATH_DATA + "wonderlist.json";

    public static void main(String[] args){
        new SevenWonders();
    }

    ViewFrame view;
    NewGameDialog dialog;
    Router router;
    PlayerView playerView;
    HighLevelView highLevelView;

    public SevenWonders() {
        view = new ViewFrame(this);
        dialog = new NewGameDialog(view,this);
        dialog.setVisible(true);
    }

    public void startGame(boolean isHost,String ipAddress) {
        // TODO: make sure this is the correct way to instantiate a router/game/player
        if(isHost){
            router = Router.getHostRouter(Router.HOST_PORT);
        }else{
            router = Router.getClientRouter(ipAddress, Router.HOST_PORT);
        }
        router.beginGame();
        dialog.setVisible(false);
        // TODO: make sure this next line is the right way of getting a player's hand
        playerView = new PlayerView((router.getLocalGame()).getPlayerById((int)router.getId()).getHand());
        highLevelView = new HighLevelView(this);
        view.setView(playerView);
    }

    public List<Card> getHand() {
        // TODO: actually get player's hand
        return new ArrayList<Card>();
    }

    public void exit() {
        System.exit(0);
    }
}
