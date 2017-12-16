package ph.edu.dlsu.mobidev.machineprojectv1;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.kevinsawicki.timeago.TimeAgo;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Created by Nikko on 11/15/2017.
 */


public class FeedHolder extends RecyclerView.ViewHolder {

    private final TextView tvFeedAchievementOwner;
    private final TextView tvFeedAchievementDesc;
    private final TextView tvFeedAchievementDateTime;
    public final TextView tvPatCount;
    public final TextView tvMehCount;
    public final LinearLayout btnPat;
    public final LinearLayout btnMeh;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public FeedHolder(View itemView) {
        super(itemView);

        tvFeedAchievementOwner = itemView.findViewById(R.id.tv_feedAchievement_owner);
        tvFeedAchievementDesc = itemView.findViewById(R.id.tv_feedAchievement_desc);
        tvFeedAchievementDateTime = itemView.findViewById(R.id.tv_feedAchievement_ts);
        tvPatCount = itemView.findViewById(R.id.tv_pat_count);
        tvMehCount = itemView.findViewById(R.id.tv_meh_count);
        btnPat = itemView.findViewById(R.id.btn_pat);
        btnMeh = itemView.findViewById(R.id.btn_meh);
    }

    public void setTimeAgo(long ts){
        ts = ts * -1;
        String time = getTimeAgo(ts);
        tvFeedAchievementDateTime.setText(time);
    }

    public void setOwner (String t) { tvFeedAchievementOwner.setText(t); }

    public void setDesc (String t){
        tvFeedAchievementDesc.setText(t);
    }

    public void setPat (int i){
        tvPatCount.setText(String.valueOf(i));
    }

    public void setMeh (int i){
        tvMehCount.setText(String.valueOf(i));
    }

    public void setTimestamp (java.sql.Timestamp ts){
        Date date = new Date(ts.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM'.' d, yyyy 'at' hh:mm aaa");
        tvFeedAchievementDateTime.setText(sdf.format(date));
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
