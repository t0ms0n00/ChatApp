package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UDPReader implements Runnable {

    private DatagramSocket socket;

    UDPReader(DatagramSocket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[1024];
        while(true){
            try {
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);  /// wait for message
                String msg = new String(receivePacket.getData());
                System.out.println(msg);
            } catch (IOException e) {
                break;
            }
        }
    }
}
