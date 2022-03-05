package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class TCPReader implements Runnable{
    private Socket socket;
    private BufferedReader in;

    TCPReader(Socket socket, BufferedReader input) {
        this.socket = socket;
        this.in = input;
    }

    @Override
    public void run() {
        String line = "";
        while(true){
            try {
                line = in.readLine();
                System.out.println(line);
            } catch (IOException e) {
                break;
            }
        }
    }
}