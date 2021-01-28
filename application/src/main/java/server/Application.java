package server;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server server = Server.getInstance();
        server.startListening();
    }
}
