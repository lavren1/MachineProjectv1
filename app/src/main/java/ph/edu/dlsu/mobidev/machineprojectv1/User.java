package ph.edu.dlsu.mobidev.machineprojectv1;

/**
 * Created by Noel Campos on 11/10/2017.
 */

public class User {
    String username;
    String email;
    String userId;

    public User(){}

    public User(String username, String email, String userId) {
        this.username = username;
        this.email = email;
        this.userId = userId;
    }

    public User(String username, String email){
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
