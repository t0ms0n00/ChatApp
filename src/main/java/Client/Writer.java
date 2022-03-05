package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Writer implements Runnable{
    private String login;
    private InetAddress address;
    private int serverPort;
    private Socket tcpSocket;
    private DatagramSocket udpSocket;
    private Scanner in;
    private PrintWriter out;

    Writer(String name, Socket tcpSocket, PrintWriter output, DatagramSocket udpSocket, InetAddress address,
           int serverPort) {
        this.login = name;
        this.address = address;
        this.serverPort = serverPort;
        this.tcpSocket = tcpSocket;
        this.udpSocket = udpSocket;
        this.in = new Scanner(System.in);
        this.out = output;
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
                else{   /// send by tcp
                    out.println(line);
                }
            } catch (IOException e) {
                break;
            }
        }
    }
}