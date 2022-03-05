package Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 8080;
        Set<UserData> users = new HashSet<>();
        Lock usersLock = new ReentrantLock();

        ServerSocket tcpSocket = new ServerSocket(port);
        DatagramSocket udpSocket = new DatagramSocket(port);

        Thread tcpListener = new Thread(new TCPChannelListener(tcpSocket, port, users, usersLock));
        Thread udpListener = new Thread(new UDPChannelListener(udpSocket, port, users, usersLock));

        /// start threads listening different communication channels
        tcpListener.start();
        udpListener.start();

        tcpListener.join();
        udpListener.join();

        /* closing sockets */
        if(udpSocket != null){
            udpSocket.close();
        }
        if(tcpSocket != null){
            tcpSocket.close();
        }
    }
}
