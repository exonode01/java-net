package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.MACAddressString;
import inet.ipaddr.ipv6.IPv6Address;
import inet.ipaddr.mac.MACAddress;

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
        while (true) {
            Server server = new Server();
            server.start(6666);
        }
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String greeting = in.readLine();
        if (greeting.startsWith("MAC:")) {
            String mac = greeting.substring(4);
            String linkLocal = createLinkLocal(mac);
            out.println(linkLocal);
        }
        if ("hello server".equals(greeting)) {
            out.println("hello client");
        }
        else {
            out.println("unrecognised greeting");
        }
    }

    private String createLinkLocal(String macStr) {
        try {
            MACAddress mac = new MACAddressString(macStr).toAddress();
            IPv6Address linkLocal = mac.toLinkLocalIPv6();
            System.out.println("converted " + mac + " to IPv6 link local " + linkLocal);
            return String.valueOf(linkLocal);
        } catch(AddressStringException e) {
            // handle invalid address string here
        }
        return null;
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        socket.close();
        serverSocket.close();
    }

}
