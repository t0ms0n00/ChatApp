package Server;

import java.io.PrintWriter;
import java.util.Objects;

public class UserData {       /// bind name with sending channel to make it reachable for any other user in server "database"
    private String login;
    private PrintWriter out;        /// for tcp

    UserData(String login){   /// only for contains and equals purposes
        this.login = login;
    }

    public void setOutputChanel(PrintWriter out){
        this.out = out;
    }

    public PrintWriter getOutputChanel(){
        return out;
    }

    public String getName(){
        return login;
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
