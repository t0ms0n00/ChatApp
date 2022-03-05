package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class TCPChannelListener implements Runnable{

    private int portNumber;
    private Set<UserData> users;
    private Lock lock;
    private ServerSocket serverSocket = null;

    TCPChannelListener(int port, Set<UserData> users, Lock userLock){
        this.portNumber = port;
        this.users = users;
        this.lock = userLock;
    }

    @Override
    public void run() {
        /* create socket */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* waiting for clients */
        System.out.println("[TCP] SERVER starts listening on port: " + portNumber);
        while(true){
            try{
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                Thread thread = new Thread(new TCPClientHandler(clientSocket, users, lock)); /// client socket pass
                thread.start();
            }
            catch (IOException e){
                break;
            }
        }

        /* closing socket */
        if(serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}