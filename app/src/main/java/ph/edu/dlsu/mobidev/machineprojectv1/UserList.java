package ph.edu.dlsu.mobidev.machineprojectv1;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noel Campos on 12/16/2017.
 */

public class UserList {
    Map<String, Boolean> users = new HashMap<>();

    public UserList(Map<String, Boolean> users) {
        this.users = users;
    }

    public UserList(){}

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }
}
