package com.steamedpears.comp3004;

import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.routing.Router;
import com.steamedpears.comp3004.views.ViewFrame;

public class SevenWonders {
    public static void main(String[] args){
        new SevenWonders();
    }

    ViewFrame view;
    SevenWondersGame game;

    public SevenWonders() {
        view = new ViewFrame(this);
    }

    public void startGame(boolean isHost,String ipAddress) {
        game =  (new Router(isHost)).getLocalGame();
    }

    public void exit() {
        System.exit(0);
    }
}
