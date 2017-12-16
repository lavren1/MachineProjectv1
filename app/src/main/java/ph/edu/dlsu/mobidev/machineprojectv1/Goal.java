package ph.edu.dlsu.mobidev.machineprojectv1;

import java.sql.Timestamp;

/**
 * Created by Noel Campos on 11/12/2017.
 */

public class Goal {
    String description;
    ph.edu.dlsu.mobidev.machineprojectv1.Timestamp timestamp;
    String username;
    String goal_id;
    long timestamps;

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

    public Goal(String description, ph.edu.dlsu.mobidev.machineprojectv1.Timestamp timestamp, String username, String goal_id) {
        this.description = description;
        this.timestamp = timestamp;
        this.username = username;
        this.goal_id = goal_id;
    }

    public Goal(String description, ph.edu.dlsu.mobidev.machineprojectv1.Timestamp timestamp, String username) {
        this.description = description;
        this.timestamp = timestamp;
        this.username = username;
    }

    public Goal(String description){
        this.description = description;
    }
    public Goal(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ph.edu.dlsu.mobidev.machineprojectv1.Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ph.edu.dlsu.mobidev.machineprojectv1.Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timestamps) {
        this.timestamps = timestamps;
    }
}
