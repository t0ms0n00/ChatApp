package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class UDPChannelListener implements Runnable{

    private int portNumber;
    private Set<UserData> users;
    private Lock lock;
    private DatagramSocket socket = null;

    UDPChannelListener(int port, Set<UserData> users, Lock userLock){
        this.portNumber = port;
        this.users = users;
        this.lock = userLock;
    }

    @Override
    public void run() {
        /* socket init */
        try {
            socket = new DatagramSocket(portNumber);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        /* working */
        System.out.println("[UDP] SERVER starts working on port: " + portNumber);
        byte[] receiveBuffer = new byte[1024];
        while(true){
            try{
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("[UDP] Received message: " + msg);
                String[] msgParts = msg.split(":",2);
                String login = msgParts[0];
                String content = msgParts[1];
                sendToOthers(content, login);
            }
            catch (IOException e){
                break;
            }
        }

        /* close socket */
        if(socket != null){
            try{
                socket.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void sendToOthers(String message, String login) throws IOException {
        lock.lock();
        for(UserData user: users){
            if(!user.equals(new UserData(login))) {  /// different user - send
                String msg_wrapper = "[UDP] " + login + ": " + message;
                byte[] sendBuffer = msg_wrapper.getBytes();
                socket.send(new DatagramPacket(sendBuffer, sendBuffer.length, user.getAddress(), user.getUdpPort()));
            }
        }
        lock.unlock();
    }
}
