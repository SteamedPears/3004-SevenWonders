package com.steamedpears.comp3004;

import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.routing.Router;
import com.steamedpears.comp3004.views.ViewFrame;

public class SevenWonders {
    public static void main(String[] args){
        new SevenWonders();
    }

    ViewFrame view;
    NewGameDialog dialog;
    Router router;

    public SevenWonders() {
        view = new ViewFrame(this);
        dialog = new NewGameDialog(view,this);
        dialog.setVisible(true);
    }

    public void startGame(boolean isHost,String ipAddress) {
        router = new Router(isHost);
    }

    public void exit() {
        System.exit(0);
    }
}
