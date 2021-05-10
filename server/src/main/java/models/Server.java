package models;

import server.ServerThread;
import server.ServerThreadsManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static Server instance;
    private static final int PORT = 9995;

    private final ServerSocket server;
    private Server() throws IOException {
        server = new ServerSocket(PORT);
    }

    public void startListening() throws IOException {
        ServerThreadsManager.getInstance();
        System.out.println("Awaiting connection...");
        while(true){
            Socket socket = server.accept();
            ServerThread newThread = new ServerThread(socket);
            ServerThreadsManager.serverThreads.add(newThread);
            newThread.start();
        }
    }


    public static Server getInstance() throws IOException {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }
}
