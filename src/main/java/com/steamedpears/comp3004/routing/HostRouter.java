package com.steamedpears.comp3004.routing;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HostRouter extends Router {
    ServerSocket serverSocket;
    List<Socket> clients;

    public HostRouter() {
        try {
            serverSocket = new ServerSocket(Router.HOST_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        clients = new ArrayList<Socket>();
    }

    @Override
    public void run(){
        while(true){
            try{
                clients.add(serverSocket.accept());
            }catch(IOException e){
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
