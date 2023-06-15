import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;

    private PrintWriter writer;

    private Server server;

    private String clientName = null;
    public ClientThread(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() throws NullPointerException{
        try {
            InputStream input  = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);

            System.out.println("New client!");
            String message;
            while ((message = reader.readLine()) != null) {
                String prefix = message.substring(0, 2);
                String postfix = message.substring(2);
                switch(prefix){
                    case "LO" -> login(postfix);
                    case "BR" -> server.broadcast(this, postfix);
                    case "WH" -> server.whisper(this, postfix);
                    case "ON" -> server.online(this);
                    case "FI" -> server.sendFile(this, postfix);
                }

                System.out.println(message);
            }
            System.out.println("client disconnected");
            server.removeClient(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void send(String message){
        writer.println(message);
    }

    public String getClientName(){
        return clientName;
    }

    public void login(String name){
        clientName = name;
        if(server != null) {
            server.online(this);
            server.broadcastLogin(this);
        }

    }

}