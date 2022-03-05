package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Writer implements Runnable{
    private String login;
    private Socket tcpSocket;
    private Scanner in;
    private PrintWriter out;
    private DatagramSocket udpSocket;
    private InetAddress address;
    private int serverPort;
    private MulticastSocket multicastSocket;
    private InetAddress group;
    private int multicastPort;

    Writer(String name, Socket tcpSocket, PrintWriter output, DatagramSocket udpSocket,
           InetAddress address, int serverPort, MulticastSocket multicastSocket, InetAddress group, int multicastPort) {
        this.login = name;

        this.tcpSocket = tcpSocket; /// tcp
        this.out = output;
        this.in = new Scanner(System.in);

        this.udpSocket = udpSocket; /// udp
        this.address = address;
        this.serverPort = serverPort;

        this.multicastSocket = multicastSocket; /// multicast
        this.group = group;
        this.multicastPort = multicastPort;

    }

    @Override
    public void run() {
        String line = "";
        while (true) {
            try {
                line = in.nextLine();
                if(line.startsWith("U ")){     /// send by udp
                    byte[] sendBuffer = (login + ":" + line.substring(2)).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, serverPort);
                    udpSocket.send(sendPacket);
                }
                else if(line.startsWith("M ")){     /// multicast
                    byte[] sendBuffer = (login + ": " + line.substring(2)).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, group, multicastPort);
                    multicastSocket.send(sendPacket);
                }
                else{   /// send by tcp
                    out.println(line);
                }
            } catch (IOException e) {
                break;
            }
        }
    }
}