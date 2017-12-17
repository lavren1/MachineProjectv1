package ph.edu.dlsu.mobidev.machineprojectv1;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by Noel Campos on 11/12/2017.
 */

public class GoalHolder extends ViewHolder{
    private final  TextView tvGoalDesc;
    private final TextView tvGoalDateTime;
    public final Button btnAchieveGoal;
    public final TextView tvGoalOptions;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public GoalHolder(View itemView) {
        super(itemView);

        tvGoalDesc = itemView.findViewById(R.id.tv_goal_desc);
        tvGoalDateTime = itemView.findViewById(R.id.tv_goal_ts);
        btnAchieveGoal = itemView.findViewById(R.id.btn_achieve_goal);
        tvGoalOptions = itemView.findViewById(R.id.tv_goal_options);
    }

    public void setDesc (String t){
        tvGoalDesc.setText(t);
    }

    public void setTimestamp (Timestamp ts){
        Date date = new Date(ts.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM'.' d, yyyy 'at' hh:mm aaa");
        tvGoalDateTime.setText(sdf.format(date));
    }

    public void setTimeAgo(long ts){
        ts = ts * -1;
        String time = getTimeAgo(ts);
        tvGoalDateTime.setText("Set "+time);
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }
}
