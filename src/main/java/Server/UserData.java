package Server;

import java.io.PrintWriter;
import java.util.Objects;

public class UserData {       /// bind name with sending channel to make it reachable for any other user in server "database"
    String login;
    PrintWriter out;

    UserData(String login){   /// only for contains and equals purposes
        this.login = login;
        this.out = null;
    }

    UserData(String login, PrintWriter out){
        this.login = login;
        this.out = out;
    }

    public void sendMessage(String sender, String message){
        this.out.println(sender + ":" + message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData that = (UserData) o;
        return Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
