package com.steamedpears.comp3004.routing;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.steamedpears.comp3004.SevenWonders;
import com.steamedpears.comp3004.models.Card;
import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;
import com.steamedpears.comp3004.models.Wonder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class HostRouter extends Router {
    ServerSocket serverSocket;
    List<Client> clients;
    JsonArray cardJSON;
    JsonArray wonderJSON;
    Map<String, Wonder> wonders;
    private Map<Player, PlayerCommand> registeredMoves;


    public HostRouter(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        clients = new ArrayList<Client>();
    }

    @Override
    public void registerMove(Player player, PlayerCommand command) {
        //TODO: make this threadsafe
        registeredMoves.put(player, command);
    }

    @Override
    public void beginGame() {
        //TODO: load in wonders, decks;

        try {
            JsonParser parser = new JsonParser();
            this.cardJSON = parser
                    .parse(new FileReader(SevenWonders.PATH_CARDS))
                    .getAsJsonObject()
                    .get(Router.PROP_ROUTE_CARDS)
                    .getAsJsonArray();
            this.wonderJSON = parser
                    .parse(new FileReader(SevenWonders.PATH_WONDERS))
                    .getAsJsonObject()
                    .get(Router.PROP_ROUTE_WONDERS)
                    .getAsJsonArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        getLocalGame().setCards(this.cardJSON);
        this.wonders = Wonder.parseWonders(this.wonderJSON);

        //TODO: build Players;
        //TODO: send all this info to each client;
        //TODO: tell each client what player they are?

    }

    @Override
    public void run(){
        if(isPlaying()){
            waitForClients();
        }else{
            listenForCommands();
        }
    }

    private void waitForClients(){
        while(true){
            try{
                clients.add(new Client(serverSocket.accept()));
            }catch(IOException e){
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private void listenForCommands(){
        for(Client client: clients){
            //TODO: ..?
        }
    }

    private void broadcastInitialConfig(){
        Gson gson = new Gson();
        JsonObject result = new JsonObject();
        result.add(PROP_ROUTE_CARDS, this.cardJSON);
        result.add(PROP_ROUTE_WONDERS, this.wonderJSON);

        JsonObject players = new JsonObject();
        for(Player player: getLocalGame().getPlayers()){
            players.addProperty("" + player.getPlayerId(), player.getWonder().getId());
        }
        result.add(PROP_ROUTE_PLAYERS, players);

        JsonArray deck = new JsonArray();
        for(List<Card> ageDeck: getLocalGame().getDeck()){
            JsonArray ageDeckJSON = new JsonArray();
            for(Card card: ageDeck){
                ageDeckJSON.add(new JsonPrimitive(card.getId()));
            }
        }
        result.add(PROP_ROUTE_DECK, deck);

        broadcast(result.toString());
    }

    private void broadcastTakeTurn(){
        broadcast(COMMAND_ROUTE_TAKE_TURN);
    }

    private void broadcastPlayerCommands(){
        broadcast(playerCommandsToJson(registeredMoves).toString());
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
