import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static Server instance;
    private static final int PORT = 9995;

    private ServerSocket server;

    private Server() throws IOException {
        server = new ServerSocket(PORT);
    }

    public void startListening() throws IOException, ClassNotFoundException {
    /*
        while (true) {
            Socket socket = server.accept();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            String message = (String) ois.readObject();
            System.out.println("Received message: " + message);


            oos.writeObject("Echo: " + message);

            ois.close();
            oos.close();
            socket.close();
        }*/

        while(true){
            System.out.println("Awaiting connection...");
            Socket socket = server.accept();
            new ServerThread(socket).start();
        }

    }


    public static Server getInstance() throws IOException {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }
}
