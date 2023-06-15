import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(5000);
        try {
            server.listen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}