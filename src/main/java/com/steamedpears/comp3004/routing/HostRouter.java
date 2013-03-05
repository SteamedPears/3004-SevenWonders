package com.steamedpears.comp3004.routing;

import com.google.gson.Gson;
import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class HostRouter extends Router {
    ServerSocket serverSocket;
    List<Client> clients;
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
        //TODO: load in wonders, decks; build Players; send all this info to each client; tell each client what player they are
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

    private void broadcastPlayerCommands(){
        //TODO: broadcast registeredMoves to clients
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
