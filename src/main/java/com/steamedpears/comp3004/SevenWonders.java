package com.steamedpears.comp3004;

import com.steamedpears.comp3004.models.*;
import com.steamedpears.comp3004.routing.*;
import com.steamedpears.comp3004.views.*;

import java.util.ArrayList;
import java.util.List;

public class SevenWonders {
    public static void main(String[] args){
        new SevenWonders();
    }

    ViewFrame view;
    NewGameDialog dialog;
    Router router;
    HandView handView;
    HighLevelView highLevelView;

    public SevenWonders() {
        view = new ViewFrame(this);
        dialog = new NewGameDialog(view,this);
        dialog.setVisible(true);
    }

    public void startGame(boolean isHost,String ipAddress) {
        // TODO: make sure this is the correct way to instantiate a router/game/player
        router = Router.getRouter(isHost);
        dialog.setVisible(false);
        handView = new HandView(this);
        highLevelView = new HighLevelView(this);
        view.setView(handView);
    }

    public List<Card> getHand() {
        // TODO: actually get player's hand
        return new ArrayList<Card>();
    }

    public void exit() {
        System.exit(0);
    }
}
