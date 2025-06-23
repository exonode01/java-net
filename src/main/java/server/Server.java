package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Einfacher Server oder irgend sowas Ähnliches.
 *
 * @author TIW
 */
public class Server {

    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(6666);
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String greeting = in.readLine();
        if ("hello server".equals(greeting)) {
            out.println("hello client");
        }
        else {
            out.println("unrecognised greeting");
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        socket.close();
        serverSocket.close();
    }

}
