package com.steamedpears.comp3004;

public class Router {

    //localGame#handleMoves should be called when every player has decided on a command
    SevenWondersGame localGame;

    public Router(SevenWondersGame localGame){
        this.localGame = localGame;
    }

    public void registerMove(Player player, PlayerCommand command){
        //TODO: relay this command to whatever networked Routers that care
    }
}
