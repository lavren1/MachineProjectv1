package ph.edu.dlsu.mobidev.machineprojectv1;

import java.sql.Timestamp;

/**
 * Created by Noel Campos on 11/12/2017.
 */

public class Goal {
    String title;
    String description;
    ph.edu.dlsu.mobidev.machineprojectv1.Timestamp timestamp;
    String username;
    String goal_id;

    public String getGoalId() {
        return goal_id;
    }

    public void setGoalId(String goal_id) {
        this.goal_id = goal_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Goal(String title, String description, ph.edu.dlsu.mobidev.machineprojectv1.Timestamp timestamp, String username, String goal_id) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.username = username;
        this.goal_id = goal_id;
    }

    public Goal(String title, String description, ph.edu.dlsu.mobidev.machineprojectv1.Timestamp timestamp, String username) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.username = username;
    }

    public Goal(String title, String description){
        this.title = title;
        this.description = description;
    }
    public Goal(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ph.edu.dlsu.mobidev.machineprojectv1.Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
