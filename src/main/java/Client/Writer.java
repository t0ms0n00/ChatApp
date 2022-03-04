package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Writer implements Runnable{
    Socket socket;
    Scanner in;
    PrintWriter out;

    Writer(Socket socket, PrintWriter output) throws IOException {
        this.socket = socket;
        this.in = new Scanner(System.in);
        this.out = output;
    }
    @Override
    public void run() {
        try {
            String line = "";
            while (!line.equals("/q")) {
                try {
                    line = in.nextLine();
                    out.println(line);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            out.println("/q");
        }
    }
}