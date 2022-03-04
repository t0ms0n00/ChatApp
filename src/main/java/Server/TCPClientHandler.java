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
        this.lock.lock();
        if(users.contains(new UserData(name))){
            out.println("error");
            this.lock.unlock();
            return false;
        }
        users.add(new UserData(name, out));  /// register my output and name
        this.lock.unlock();

        out.println(name);
        login = name;
        return true;
    }

    public void unregister(){
        this.lock.lock();
        users.remove(new UserData(login));
        this.lock.unlock();

        out.println("/q");  /// signal to quit
    }

    public void sendToOthers(String message){
        this.lock.lock();
        for(UserData user: users){
            if(!user.equals(new UserData(login)))     /// different user - send
                user.sendMessage(login, message);
        }
        this.lock.unlock();
    }
}