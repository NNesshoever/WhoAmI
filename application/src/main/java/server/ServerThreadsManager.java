package server;

import java.io.IOException;
import java.util.ArrayList;

public class ServerThreadsManager {
    private static ServerThreadsManager instance;
    static ArrayList<ServerThread> serverThreads = new ArrayList<ServerThread>();

    public static ServerThreadsManager getInstance() throws IOException {
        if (instance == null) {
            instance = new ServerThreadsManager();
        }
        return instance;
    }

    public static ServerThread getThreadByClientID(int id){
        for(int i = 0; i < serverThreads.size(); i++){
            if(serverThreads.get(i).getClientId() == id){
                return  serverThreads.get(i);
            }
        }

        return null;
    }
}
