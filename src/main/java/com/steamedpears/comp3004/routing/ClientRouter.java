package com.steamedpears.comp3004.routing;

import com.steamedpears.comp3004.models.Player;
import com.steamedpears.comp3004.models.PlayerCommand;

import java.io.IOException;
import java.net.Socket;

class ClientRouter extends Router {
    Socket host;

    public ClientRouter(String ipAddress, int port){
        try {
            host = new Socket(ipAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void run(){
        //TODO: ...?
    }

    @Override
    public void start(){

    }

    @Override
    public void registerMove(Player player, PlayerCommand command) {
        //TODO: send this to the host
    }

    @Override
    public void beginGame() {
        //TODO: ...? is this meaningful for the client?
    }
}
