package server;

import Dtos.PersonDto;

import java.io.IOException;
import java.util.List;

public class Application {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server server = Server.getInstance();
        server.startListening();
    }
}
