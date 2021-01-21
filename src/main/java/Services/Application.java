import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Application {


    public static void main(String[] args) throws IOException {
        Client client = Client.getInstance();
        client.read();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            client.sendText(reader.readLine());
        }

    }
}
