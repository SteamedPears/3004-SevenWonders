package com.steamedpears.comp3004;

import java.util.Map;

public class Router {

    //localGame#handleMoves should be called when every player has decided on a command
    private SevenWondersGame localGame;
    private Map<Player, PlayerCommand> registeredMoves;
    private boolean host;

    public Router(boolean host){
        this.localGame = new SevenWondersGame(this);
        this.host = host;
    }

    public void registerMove(Player player, PlayerCommand command){
        //TODO: relay this command to whatever networked Routers that care (probably just host's Router)
    }

    public void beginGame(){
        //TODO: if host, tell other routers to beginGame

        //calls localGame.takeTurn in different thread
        localGame.start();
    }

    public SevenWondersGame getLocalGame(){
        return localGame;
    }
}
