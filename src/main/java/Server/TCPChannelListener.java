package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

public class TCPChannelListener implements Runnable{
    int portNumber;
    Set<UserData> users;

    TCPChannelListener(int port, Set<UserData> users){
        this.portNumber = port;
        this.users = users;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("[TCP] SERVER starts listening on port: " + portNumber);
            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                Thread thread = new Thread(new TCPClientHandler(clientSocket, users)); /// client socket pass
                thread.start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}