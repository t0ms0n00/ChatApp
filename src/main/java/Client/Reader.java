package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class Reader implements Runnable{
    Socket socket;
    BufferedReader in;

    Reader(Socket socket, BufferedReader input) throws IOException {
        this.socket = socket;
        this.in = input;
    }

    @Override
    public void run() {
        try {
            String line = "";
            while(true){
                try {
                    line = in.readLine();
                    if(line.equals("/q")) break;
                    System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}