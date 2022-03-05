package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.Arrays;

public class MulticastReader implements Runnable{

    private MulticastSocket socket;
    private String name;

    MulticastReader(MulticastSocket socket, String name){
        this.socket = socket;
        this.name = name;
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[1024];
        while(true){
            try {
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);  /// wait for message
                String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String senderLogin = msg.split(":", 2)[0];
                if(!senderLogin.equals(name)) System.out.println("[MUL] " + msg);   /// to prevent showing your messages
            } catch (IOException e) {
                break;
            }
        }
    }
}
