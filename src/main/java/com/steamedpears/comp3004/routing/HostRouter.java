package com.steamedpears.comp3004.routing;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.SevenWondersGame;
import com.steamedpears.comp3004.models.Wonder;
import com.steamedpears.comp3004.models.players.HumanPlayer;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class HostRouter extends Router {
    private ServerSocket serverSocket;
    private List<Client> clients;
    private JsonArray cardJSON;
    private JsonArray wonderJSON;
    private Map<Player, PlayerCommand> registeredMoves;
    private ExecutorService pool;
    private Future lobbyThread;
    private Future listenerThread;
    private int maxPlayers;

    private static Logger log = Logger.getLogger(Router.class);

    public HostRouter(int port, int maxPlayers) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        this.clients = new ArrayList<Client>();
        this.maxPlayers = maxPlayers;
        localPlayerId = 0;

        registeredMoves = new HashMap<Player, PlayerCommand>();
        pool = Executors.newFixedThreadPool(SevenWonders.MAX_PLAYERS+2);

        start();
    }

    @Override
    public synchronized void registerMove(Player player, PlayerCommand command) {
        log.debug("registering move");
        registeredMoves.put(player, command);
    }

    @Override
    public void beginGame() {
        log.debug("Beginning game");
        lobbyThread.cancel(true);

        loadModelConfigs();

        SevenWondersGame game = getLocalGame();
        game.setDeck(game.generateRandomDeck(maxPlayers));

        constructPlayers();

        this.setPlaying(true);

        listenerThread = pool.submit(new Runnable() {
            public void run() {
                listenForCommands();
            }
        });

        broadcastInitialConfig();

        startNextTurn();
    }

    private void loadModelConfigs(){
        log.debug("Loading model config");

        JsonParser parser = new JsonParser();
        this.cardJSON = parser
                .parse(new InputStreamReader(HostRouter.class.getResourceAsStream(SevenWonders.PATH_CARDS)))
                .getAsJsonObject()
                .get(Router.PROP_ROUTE_CARDS)
                .getAsJsonArray();
        this.wonderJSON = parser
                .parse(new InputStreamReader(HostRouter.class.getResourceAsStream(SevenWonders.PATH_WONDERS)))
                .getAsJsonObject()
                .get(Router.PROP_ROUTE_WONDERS)
                .getAsJsonArray();


        getLocalGame().setCards(this.cardJSON);
        getLocalGame().setWonders(this.wonderJSON);
    }

    private void constructPlayers(){
        log.debug("Constructing players");
        List<Wonder> wonderList = new ArrayList<Wonder>();
        wonderList.addAll(getLocalGame().getWonders().values());

        Collections.shuffle(wonderList);

        SevenWondersGame game = getLocalGame();
        for(int i=0; i<maxPlayers; ++i){
            Player player;
            Wonder wonder = wonderList.get(i);
            wonder.randomizeSide();
            if(i<=clients.size()){
                player = new HumanPlayer(wonder, game);
            }else{
                player = Player.newAIPlayer(wonder, game);
            }
            if(i==0 || i>=clients.size()){
                game.addLocalPlayer(player);
            }
        }

        List<Player> players = game.getPlayers();
        Player oldPlayer = players.get(players.size()-1);
        for(Player newPlayer: players){
            newPlayer.setPlayerLeft(oldPlayer);
            oldPlayer.setPlayerRight(newPlayer);
        }
    }

    public void start(){
        log.debug("Starting Host Router");
        lobbyThread = pool.submit(new Runnable() {
            public void run() {
                waitForClients();
            }
        });
    }



    private void waitForClients(){
        log.debug("Waiting for clients to connect");
        while(!Thread.interrupted()){
            try{
                clients.add(new Client(serverSocket.accept()));
            }catch(IOException e){
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private void listenForCommands(){
        log.debug("Listening for commands");
        while(true){
            SevenWondersGame game = getLocalGame();
            if(registeredMoves.size()==game.getPlayers().size()){
                log.debug("All player commands received");

                boolean gameOver = game.applyCommands(registeredMoves);

                broadcastPlayerCommands();

                //TODO: wait for clients to actually respond before doing this
                try {
                    log.debug("Waiting for game to finish up");
                    while(!game.isDone()){
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    System.exit(-1);
                }

                if(!gameOver){
                    startNextTurn();
                }else{
                    log.info("game is over");
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private void startNextTurn(){
        log.debug("Starting next turn");
        getLocalGame().takeTurns();
        broadcastTakeTurn();
    }

    private void broadcastInitialConfig(){
        Gson gson = new Gson();
        JsonObject result = new JsonObject();
        result.add(PROP_ROUTE_CARDS, this.cardJSON);
        result.add(PROP_ROUTE_WONDERS, this.wonderJSON);

        result.add(PROP_ROUTE_PLAYERS, getLocalGame().getPlayersAsJSON());

        result.add(PROP_ROUTE_DECK, getLocalGame().getDeckAsJSON());

        broadcast(result.toString());
    }

    private void broadcastTakeTurn(){
        broadcast(COMMAND_ROUTE_TAKE_TURN);
    }

    private void broadcastPlayerCommands(){
        broadcast(playerCommandsToJson(registeredMoves).toString());
        registeredMoves = new HashMap<Player, PlayerCommand>();
    }

    private void broadcast(String message){
        for(Client client: clients){
            client.sendMessage(message);
        }
    }

    private class Client extends Thread{
        private BufferedReader in;
        private PrintWriter out;
        private Socket client;

        public Client(Socket client){
            try {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        @Override
        public void run(){
            //TODO: ...?
        }

        public void sendMessage(String message){
            out.print(message);
            out.flush();
        }
    }


}
