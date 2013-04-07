package com.steamedpears.comp3004.routing;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.steamedpears.comp3004.models.players.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class ClientRouter extends Router implements Runnable{
    private static Logger log = Logger.getLogger(ClientRouter.class);

    private SocketWrapper socket;
    private JsonParser parser;
    private Executor pool = Executors.newFixedThreadPool(1);
    private int totalHumanPlayers;

    /**
     * Creates a new ClientRouter talking to the given ip and port
     * @param ipAddress the ip of the Host
     * @param port the port of the Host
     */
    public ClientRouter(String ipAddress, int port) {
        try {
            this.socket = new SocketWrapper(new Socket(ipAddress, port));
            this.parser = new JsonParser();
            this.totalHumanPlayers = 0;
            start();
        } catch (IOException e) {
            log.error("Could not obtain connection to host", e);
            System.exit(-1);
        }
    }

    @Override
    public synchronized void registerMove(Player player, PlayerCommand command) {
        log.debug("Registering command from player: "+player.getPlayerId()+" - "+command);
        Map<Player, PlayerCommand> commands = new HashMap<Player, PlayerCommand>();
        commands.put(player, command);
        sendCommands(commands);
    }

    private void sendOkay(){
        log.debug("Telling host I am ready for more");
        JsonObject obj = new JsonObject();
        obj.addProperty(COMMAND_ROUTE_OK, true);
        send(obj.toString());
    }

    private void sendCommands(Map<Player, PlayerCommand> commands){
        send(playerCommandsToJson(commands).toString());
    }

    private void send(String string){
        log.debug("Sending JSON: "+string);
        socket.println(string);
    }

    private void waitForTakeTurn(){
        log.debug("Waiting for host to give 'okay' to take turn");
        String line = socket.readLine();
        log.debug("got: "+line);
        if(!socket.isValid()) {
            // deal with host disconnect here
            log.error("Host disconnect, BAIL OUT");
            System.exit(-1);
        }
        try {
            JsonElement elem = parser.parse(line);
        } catch(IllegalArgumentException e) {
            log.error("Illegal argument while parsing command",e);
            cleanup();
        }
        log.debug("Got 'okay' to take turn; taking turn");
        getLocalGame().takeTurns();
    }

    private boolean waitForCommands(){
        log.debug("Waiting for host to send back all the player commands");
        Map<Player, PlayerCommand> commands = new HashMap<Player, PlayerCommand>();
        String line = socket.readLine();
        log.debug("got: " + line);
        if(!socket.isValid()) {
            log.error("Host disconnect, BAIL OUT");
            System.exit(-1);
        }
        try {
            JsonObject obj = parser.parse(line).getAsJsonObject();
            log.debug("Got player commands; applying commands: "+obj.toString());
            commands = jsonToPlayerCommands(obj);
        } catch(Exception e) {
            log.error("Exception while parsing commands",e);
            cleanup();
        }
        return getLocalGame().applyCommands(commands);
    }

    private void waitForInitialConfig(){
        log.debug("Waiting for initial config from host");
        String line = socket.readLine();
        log.debug("got: " + line);
        if(!socket.isValid()) {
            log.error("Host disconnect while waiting for initial config, BAIL OUT");
            System.exit(-1);
        }
        try {
            JsonObject obj = parser.parse(line).getAsJsonObject();

            log.debug("Got initial config from host, building model: "+obj.toString());
            SevenWondersGame game = getLocalGame();
            game.setCards(obj.getAsJsonArray(PROP_ROUTE_CARDS));
            game.setDeck(obj.getAsJsonArray(PROP_ROUTE_DECK));
            game.setWonders(obj.getAsJsonArray(PROP_ROUTE_WONDERS));
            game.setPlayers(obj.getAsJsonObject(PROP_ROUTE_PLAYERS));
            log.debug("Model built");
        } catch(Exception e) {
            log.error("Exception while parsing initial config");
            cleanup();
        }
    }

    private void waitForLocalPlayerId() {
        log.debug("Getting localPlayerId from host");
        String line = socket.readLine();
        log.debug("got: " + line);
        if(!socket.isValid()) {
            log.error("Host disconnect while waiting for initial config, BAIL OUT");
            System.exit(-1);
        }
        try {
            JsonObject obj = parser.parse(line).getAsJsonObject();
            setLocalPlayerId(obj.get(PROP_ROUTE_YOU_ARE).getAsInt());
            log.debug("Got localPlayerId: "+getLocalPlayerId());
        } catch(Exception e) {
            log.error("IO Exception while waiting for local player ID");
            cleanup();
        }
    }

    @Override
    public void beginGame() {
        //This method doesn't seem to have much meaning for the ClientRouter
    }

    @Override
    public int getTotalHumanPlayers() {
        //TODO: have the host send this information to the clients
        return totalHumanPlayers;
    }

    private void start(){
        pool.execute(this);
    }

    @Override
    public void run() {
        log.debug("Starting Client Router");
        waitForLocalPlayerId();
        waitForInitialConfig();
        setPlaying(true);
        boolean gameOver = false;
        while(!gameOver){
            waitForTakeTurn();
            gameOver = waitForCommands();
            sendOkay();
        }
        log.debug("Game over; client router stopping");
    }

    @Override
    public void cleanup() {
        super.cleanup();
        socket.close();
        announceChange(this);
    }

}
