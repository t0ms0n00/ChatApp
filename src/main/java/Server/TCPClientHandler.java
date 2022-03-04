package Server;

import java.io.*;
import java.net.Socket;
import java.util.Set;

public class TCPClientHandler implements Runnable{
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    Set<UserData> users;
    String login;

    TCPClientHandler(Socket socket, Set<UserData> users) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.users = users;
    }

    @Override
    public void run() {
        String msg = "";
        while(true){
            try {
                msg = in.readLine();
                System.out.println("Received message " + msg);

                if(msg.equals("/q")) { /// leave command
                    sendToOthers("* leaves the chat *");
                    unregister();
                    break;
                }
                else if(msg.startsWith("--register")){   /// register command
                    String[] messageSplit = msg.split(":", 2);
                    String name = messageSplit[1];
                    if(register(name)){
                        sendToOthers("* enters the chat *");
                    }
                    else{ /// when registration failed
                        break;
                    }
                }
                else{ /// TCP communication
                    sendToOthers(msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean register(String name){
        if(users.contains(new UserData(name))){
            out.println("error");
            return false;
        }
        users.add(new UserData(name, out));  /// register my output and name
        out.println(name);
        login = name;
        return true;
    }

    public void unregister(){
        users.remove(new UserData(login));
        out.println("/q");  /// signal to quit
    }

    public void sendToOthers(String message){
        for(UserData user: users){
            if(!user.equals(new UserData(login)))     /// different user - send
                user.sendMessage(login, message);
        }
    }
}