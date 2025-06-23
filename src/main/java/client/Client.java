package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import server.Server;

/**
 * TODO (TIW) Beschreibung der Klasse ...
 *
 * @author iFD GmbH
 * @author TIW
 */
public class Client {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private int cIp;
    private int v;
    private char cMac;
    private Server server;

    public Client(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //server = new Server();
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        System.out.println(resp);
        return resp;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public void receiveIp() {
        Scanner scanner = new Scanner(System.in);
        int na, mask;
        System.out.print("VLAN-Nr.: ");
        v = scanner.nextInt();

        switch (v) {
            case 1:
                na = 0;
                mask = 26;
                break;
            case 2:
                na = 64;
                mask = 26;
                break;
            case 3:
                na = 128;
                mask = 27;
                break;
            case 4:
                na = 160;
                mask = 27;
                break;
            case 5:
                na = 192;
                mask = 27;
                break;
            case 6:
                na = 224;
                mask = 28;
                break;
            case 7:
                na = 240;
                mask = 29;
                break;
            case 8:
                na = 248;
                mask = 30;
                break;
            case 9:
                na = 252;
                mask = 30;
                break;
            default:
                na = 0;
                mask = 24;
        }
        //cIp = server.simpleDhcp(na, mask);
        //System.out.println("IP: 192.168.10." + cIp);
    }

    public void receiveV6() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Eigene Mac:");
        String mac = scanner.nextLine();
        String resp = sendMessage("MAC:" + mac);
        System.out.println("Link Lokal Adresse: " + resp);
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1", 6666);
        //client.sendMessage("hello server");
        client.receiveV6();
        //client.receiveIp();
    }
}