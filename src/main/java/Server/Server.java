package Server;

import java.util.HashSet;
import java.util.Set;

public class Server {
    public static void main(String[] args) {
        int port = 8080;
        Set<UserData> users = new HashSet<>();
        Thread tcpListener = new Thread(new TCPChannelListener(port, users));

        /// start threads
        tcpListener.start();
    }
}
