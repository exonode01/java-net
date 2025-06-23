package client;

/**
 * TODO (TIW) Beschreibung der Klasse ...
 *
 * @author iFD GmbH
 * @author TIW
 */
public class Client {

    private int cIp;
    private int v;
    private char cMac;
    private Server server;

    public Client() {
        server = new Server();
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
        cIp = server.simpleDhcp(na, mask);
        System.out.println("IP: 192.168.10." + cIp);
    }

    public void receiveV6() {
        System.out.println("Eigene Mac:");
        System.out.println("Link Lokal Adresse:");
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.receiveIp();
        client.receiveV6();
    }
}