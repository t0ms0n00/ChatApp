package Server;

import java.io.*;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class TCPClientHandler implements Runnable{

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Set<UserData> users;
    private String login;
    private Lock lock;

    TCPClientHandler(Socket socket, Set<UserData> users, Lock usersLock) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.users = users;
        this.lock = usersLock;
    }

    @Override
    public void run() {
        String msg = "";
        while(true){
            try {
                msg = in.readLine();
                System.out.println("[TCP] Received message " + msg);

                /// special commands first, anything else is message
                if(msg.startsWith("/register")){   /// register command
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
                sendToOthers("* leaves the chat *");
                unregister();
                break;
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean register(String name){
        lock.lock();
        if(users.contains(new UserData(name))){
            out.println("/error");
            lock.unlock();
            return false;
        }
        UserData user = new UserData(name); /// register my output and name
        user.setOutputChanel(out);
        users.add(user);
        lock.unlock();

        out.println(name);
        login = name;
        return true;
    }

    public void unregister(){
        lock.lock();
        users.remove(new UserData(login));
        lock.unlock();
    }

    public void sendToOthers(String message){
        lock.lock();
        for(UserData user: users){
            if(!user.equals(new UserData(login)))     /// different user - send
                user.getOutputChanel().println(login + ": " + message);
        }
        lock.unlock();
    }
}