import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static Socket socket;
    private static final int PORT = 9995;
    private int clientId = -1;
    private DataOutputStream dos;
    private DataInputStream dis;

    private static Client instance;

    private Client() throws IOException {
        socket = new Socket(InetAddress.getLocalHost().getHostName(), PORT);
        dos = new DataOutputStream(socket.getOutputStream());

        dos.writeUTF("/InitClient");
        dos.flush();
        dis = new DataInputStream(socket.getInputStream());
        String id = dis.readUTF();
        clientId = Integer.parseInt(id);
    }


    /**
     * Sends a text message to the server and outputs the response message.
     *
     * @param message The message to send to the server.
     * @throws IOException When stream usage fails.
     */
    public void sendText(String message) throws IOException {
        if (clientId > 0) {
            dos.writeUTF(message);
            dos.flush();
        }
    }

    public void read() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    if (dis.available() > 0) {
                        System.out.println(dis.readUTF());
                    }
                } catch (IOException ignored) {

                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {

                }
            }

        });
        t.start();
    }


    public static Client getInstance() throws IOException {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }
}
