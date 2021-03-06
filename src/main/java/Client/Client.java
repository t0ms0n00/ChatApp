package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        String name = args[0];
        String serverAddress = "localhost";
        int serverPort = 8080;
        String multicastAddress = "230.0.0.0";
        int multicastPort = 8081;

        Socket tcpSocket;
        DatagramSocket udpSocket;
        MulticastSocket multicastSocket;

        Thread tcpReader;
        Thread udpReader;
        Thread multicastReader;
        Thread writer;

        tcpSocket = new Socket(serverAddress, serverPort);
        BufferedReader input = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
        PrintWriter output = new PrintWriter(tcpSocket.getOutputStream(), true);

        udpSocket = new DatagramSocket();
        InetAddress address = InetAddress.getByName(serverAddress);

        multicastSocket = new MulticastSocket(multicastPort);
        InetAddress group = InetAddress.getByName(multicastAddress);
        multicastSocket.joinGroup(group);

        if(register(name, input, output, udpSocket, address)){ /// if registered successfully start threads
            tcpReader = new Thread(new TCPReader(tcpSocket, input));
            udpReader = new Thread(new UDPReader(udpSocket));
            multicastReader = new Thread(new MulticastReader(multicastSocket, name));
            writer = new Thread(new Writer(name, tcpSocket, output, udpSocket, address, serverPort,
                    multicastSocket, group, multicastPort));

            tcpReader.start();
            udpReader.start();
            multicastReader.start();
            writer.start();

            tcpReader.join();   /// wait till all threads finish job - when client is ending connection
            udpReader.join();
            multicastReader.join();
            writer.join();

        }

        if(multicastSocket != null){
            multicastSocket.leaveGroup(group);
            multicastSocket.close();
        }

        if(udpSocket != null){
            udpSocket.close();
        }
        if(tcpSocket != null){
            tcpSocket.close();
        }
    }

    public static boolean register(String name, BufferedReader in, PrintWriter out, DatagramSocket udpSocket, InetAddress address) throws IOException {
        int port = udpSocket.getLocalPort();
        out.println("/register--" + name + "--" + address.getHostAddress() + "--" + port);   /// register cmd flag and name as info for server
        String response = in.readLine();    /// wait till server check if u can register
        if(response.equals("/error_1")) {   /// errors handle
            System.out.println("The login is taken!");
            return false;
        }
        if(response.equals("/error_2")) {
            System.out.println("Unknown host address");
            return false;
        }
        System.out.println("You successfully logged as " + response);
        return true;
    }
}