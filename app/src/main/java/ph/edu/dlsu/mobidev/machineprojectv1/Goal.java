package ph.edu.dlsu.mobidev.machineprojectv1;

import java.sql.Timestamp;

/**
 * Created by Noel Campos on 11/12/2017.
 */

public class Goal {
    String title;
    String description;
    Timestamp timestamp;

    public Goal(String title, String description, Timestamp timestamp) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
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
