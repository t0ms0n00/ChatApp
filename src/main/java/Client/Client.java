package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static boolean register(String name, BufferedReader in, PrintWriter out) throws IOException {
        out.println("Name:" + name);
        String response = in.readLine();
        if(!response.equals("error")) {
            System.out.println("You succesfully registered with login " + response);
            return true;
        }
        System.out.println("The login is taken " + response);
        return false;
    }

    public static void main(String[] args) throws IOException {
        int serverPort = 8080;
        String serverAddress = "localhost";
        Socket socket = null;
        String name = args[0];

        try{
            socket = new Socket(serverAddress, serverPort);

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            /// registered Client succesfully
            if(register(name, input, output)){
                Thread reader = new Thread(new Reader(socket, input));
                Thread writer = new Thread(new Writer(socket, output));

                reader.start();
                writer.start();

                reader.join();      /// writer and reader finish job with receiving /q, but socket will be closed later
                writer.join();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
            }
        }
    }
}