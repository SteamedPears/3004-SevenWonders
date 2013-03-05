package com.steamedpears.comp3004.routing;

import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Router extends Thread{

    public static Router getRouter(boolean host){
        if(host){
            return new HostRouter();
        }else{
            return new ClientRouter();
        }
    }

    public static final int HOST_PORT = 1567;

    //localGame#handleMoves should be called when every player has decided on a command
    private SevenWondersGame localGame;
    private Map<Player, PlayerCommand> registeredMoves;

    protected Router() {
        this.localGame = new SevenWondersGame(this);
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
