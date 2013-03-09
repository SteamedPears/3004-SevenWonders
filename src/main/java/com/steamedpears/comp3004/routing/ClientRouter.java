package com.steamedpears.comp3004.routing;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class ClientRouter extends Router implements Runnable{
    private static Logger log = Logger.getLogger(ClientRouter.class);

    private Socket host;
    private BufferedReader in;
    private PrintWriter out;
    private JsonParser parser;
    private Executor pool = Executors.newFixedThreadPool(1);

    public ClientRouter(String ipAddress, int port) {
        try {
            host = new Socket(ipAddress, port);
            in = new BufferedReader(new InputStreamReader(host.getInputStream()));
            out = new PrintWriter(host.getOutputStream());
            start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void registerMove(Player player, PlayerCommand command) {
        Map<Player, PlayerCommand> commands = new HashMap<Player, PlayerCommand>();
        commands.put(player, command);
        sendCommands(commands);
    }

    private void sendOkay(){
        out.print(COMMAND_ROUTE_OK);
        out.flush();
    }

    private void sendCommands(Map<Player, PlayerCommand> commands){
        out.print(playerCommandsToJson(commands).toString());
        out.flush();
    }

    private void waitForTakeTurn(){
        try {
            while(!in.readLine().equals(COMMAND_ROUTE_TAKE_TURN));
        } catch (IOException e) {
            log.error("Exception while waiting for 'okay' to take turn", e);
            System.exit(-1);
        }
    }

    private void waitForCommands(){
        JsonObject obj = parser.parse(in).getAsJsonObject();
        Map<Player, PlayerCommand> commands = jsonToPlayerCommands(obj);
        getLocalGame().applyCommands(commands);
    }

    private void waitForInitialConfig(){
        JsonObject obj = parser.parse(in).getAsJsonObject();

        SevenWondersGame game = getLocalGame();
        game.setCards(obj.getAsJsonArray(PROP_ROUTE_CARDS));
        game.setDeck(obj.getAsJsonArray(PROP_ROUTE_DECK));
        game.setWonders(obj.getAsJsonArray(PROP_ROUTE_WONDERS));
        game.setPlayers(obj.getAsJsonObject(PROP_ROUTE_PLAYERS));

    }

    private void waitForLocalPlayerId() {
        JsonObject obj = parser.parse(in).getAsJsonObject();
        localPlayerId = obj.get(PROP_ROUTE_YOU_ARE).getAsInt();
    }

    @Override
    public void beginGame() {
        //This method doesn't seem to have much meaning for the ClientRouter
    }

    private void start(){
        pool.execute(this);
    }

    @Override
    public void run() {
        log.debug("Starting Client Router");
        waitForLocalPlayerId();
        waitForInitialConfig();
        while(true){
            waitForTakeTurn();
            getLocalGame().takeTurns();
            waitForCommands();
            sendOkay();
        }
    }
}
