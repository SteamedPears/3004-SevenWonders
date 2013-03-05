package com.steamedpears.comp3004.routing;

import com.google.gson.JsonObject;
import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Router extends Thread{

    public static Router getHostRouter(int port){
        return new HostRouter(port);
    }

    public static Router getClientRouter(String ipAddress, int port){
        return new ClientRouter(ipAddress, port);
    }

    public static final int HOST_PORT = 1567;
    public static String PROP_ROUTE_CARDS = "cards";
    public static String PROP_ROUTE_WONDERS = "wonders";
    public static String PROP_ROUTE_DECK = "deck";


    //localGame#handleMoves should be called when every player has decided on a command
    private SevenWondersGame localGame;
    private boolean playing;

    protected Router() {
        this.localGame = new SevenWondersGame(this);
        playing = false;
    }

    public abstract void registerMove(Player player, PlayerCommand command);

    public abstract void beginGame();

    public SevenWondersGame getLocalGame(){
        return localGame;
    }

    public boolean isPlaying(){
        return playing;
    }

    protected JsonObject playerCommandsToJson(Map<Player, PlayerCommand> commands){
        //TODO: this
        return null;
    }

    protected Map<Player, PlayerCommand> jsonToPlayerCommands(JsonObject obj){
        //TODO: this
        return null;
    }
}
