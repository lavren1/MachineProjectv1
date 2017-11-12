package ph.edu.dlsu.mobidev.machineprojectv1;

import java.sql.Timestamp;

/**
 * Created by Noel Campos on 11/12/2017.
 */

public class Goal {
    String title;
    String description;
    Timestamp timestamp;
    String username;
    String goalId;

    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Goal(String title, String description, Timestamp timestamp, String username) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.username = username;
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

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
