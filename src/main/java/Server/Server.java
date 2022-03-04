package Server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    public static void main(String[] args) {
        int port = 8080;
        Set<UserData> users = new HashSet<>();
        Lock usersLock = new ReentrantLock();
        Thread tcpListener = new Thread(new TCPChannelListener(port, users, usersLock));

        /// start threads
        tcpListener.start();
    }
}
