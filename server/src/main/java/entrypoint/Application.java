package entrypoint;

import models.Server;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        Server server = Server.getInstance();
        server.startListening();
    }
}
