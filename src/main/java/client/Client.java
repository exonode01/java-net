package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public void receiveGlobalUnicast() {
        String ipv6 = "2001:0005:0200:0001:0080:2000:0010:01FF";
        System.out.println("IPv6-Adresse des Client [Global Unicast]");
        System.out.println("Ungekürzt: " + ipv6);

        // 1. IPv6 splitten
        String[] segments = ipv6.split(":");

        // 2. Führende Nullen entfernen
        for (int i = 0; i < segments.length; i++) {
            segments[i] = segments[i].replaceFirst("^0{1,3}", ""); // max 3 führende Nullen entfernen
            if (segments[i].isEmpty()) segments[i] = "0";
        }

        // 3. Längsten Block aus aufeinanderfolgenden "0"-Segmenten finden
        int bestStart = -1, bestLen = 0;
        int currStart = -1, currLen = 0;

        for (int i = 0; i < segments.length; i++) {
            if (segments[i].equals("0")) {
                if (currStart == -1) {
                    currStart = i;
                    currLen = 1;
                } else {
                    currLen++;
                }

                if (currLen > bestLen) {
                    bestStart = currStart;
                    bestLen = currLen;
                }
            } else {
                currStart = -1;
                currLen = 0;
            }
        }

        // 4. Komprimierung anwenden: longest zero block → ::
        StringBuilder result = new StringBuilder();
        boolean compressed = false;

        for (int i = 0; i < segments.length; ) {
            if (i == bestStart && !compressed && bestLen > 1) {
                result.append("::");
                i += bestLen;
                compressed = true;
            } else {
                result.append(segments[i]);
                i++;
                if (i < segments.length) result.append(":");
            }
        }

        String finalResult = result.toString();

        while (finalResult.contains(":::")) {
            finalResult = finalResult.replace(":::", "::");
        }

        // 5. Ausgabe
        System.out.println("Gekürzt : " + finalResult);
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1", 6666);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Auswahl IPv4 oder IPv6 [1 oder 2]");
        String select = scanner.nextLine();
        if (select.equals("1")) {
            client.receiveIp();
        } else if (select.equals("2")) {
            System.out.println("Auswahl Link_Local oder Global Unicast [1 oder 2]");
            select = scanner.nextLine();
            if (select.equals("1")) {
                client.receiveV6();
            } else if (select.equals("2")) {
                client.receiveGlobalUnicast();
            } else {
                System.err.println("Keine gültige Eingabe!");
            }
        } else {
            System.err.println("Keine gültige Eingabe!");
        }
    }
}