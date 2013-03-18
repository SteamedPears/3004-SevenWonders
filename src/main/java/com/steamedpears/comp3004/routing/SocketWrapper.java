package com.steamedpears.comp3004.routing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketWrapper {
    private Socket socket;
    private boolean valid;
    private BufferedReader reader;
    private PrintWriter writer;

    public SocketWrapper(Socket socket) {
        this.socket = socket;
        this.valid = true;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(Exception e) {
            close();
        }
        try {
            this.writer = new PrintWriter(socket.getOutputStream(),true);
        } catch(Exception e) {
            close();
        }
    }

    /**
     * Read a line from the socket
     * @return the line read from the socket, or null if socket is closed
     */
    public String readLine() {
        if(!valid) return null;
        try {
            while(!reader.ready()) {}
            return reader.readLine();
        } catch(Exception e) {
            close();
        }
        return null;
    }

    /**
     * Write a string to the socket
     * @param line the string to write to the socket
     */
    public void println(String line) {
        if(!valid) return;
        writer.println(line);
    }

    /**
     * Closes the socket and all associated readers
     */
    public void close() {
        this.valid = false;
        try {
            this.reader.close();
        } catch(Exception e) {}
        try {
            this.writer.close();
        } catch(Exception e) {}
        try {
            this.socket.close();
        } catch(Exception e) {}
        this.reader = null;
        this.writer = null;
        this.socket = null;
    }

    public boolean isValid() { return valid; }
}
