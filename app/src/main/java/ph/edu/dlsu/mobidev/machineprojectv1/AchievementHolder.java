package ph.edu.dlsu.mobidev.machineprojectv1;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by Nikko on 11/12/2017.
 */

public class AchievementHolder extends ViewHolder {
    private final TextView tvAchievementDesc;
    private final TextView tvAchievementDateTime;
    public final TextView tvAchievementPats;
    public final TextView tvAchievementMehs;
    public final TextView tvAchievementOptions;
    public final LinearLayout btnAchievementPat;
    public final LinearLayout btnAchievementMeh;
    public final ImageView ivAchievementPat;
    public final ImageView ivAchievementMeh;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public AchievementHolder(View itemView) {
        super(itemView);

        tvAchievementDesc = itemView.findViewById(R.id.tv_achievement_desc);
        tvAchievementDateTime = itemView.findViewById(R.id.tv_achievement_ts);
        tvAchievementPats = itemView.findViewById(R.id.tv_ach_pat_count);
        tvAchievementMehs = itemView.findViewById(R.id.tv_ach_meh_count);
        tvAchievementOptions = itemView.findViewById(R.id.tv_achievement_options);
        ivAchievementMeh = itemView.findViewById(R.id.achievement_meh);
        ivAchievementPat = itemView.findViewById(R.id.achievement_pat);
        btnAchievementPat = itemView.findViewById(R.id.btn_ach_pat);
        btnAchievementMeh = itemView.findViewById(R.id.btn_ach_meh);
    }


    public void setDesc (String t){
        tvAchievementDesc.setText(t);
    }

    public void setReactImageResource(int i){
        if(i == 1){
            ivAchievementPat.setImageResource(R.drawable.crap);
        }
        else if(i == 2){
            ivAchievementPat.setImageResource(R.drawable.crap_light_er);
        }
        else if(i == 3){
            ivAchievementMeh.setImageResource(R.drawable.smolermeh);
        }
        else if(i == 4){
            ivAchievementMeh.setImageResource(R.drawable.smolermeh_light_er);
        }
    }

    public void setDateAchieved(long ts){
        ts = ts * -1;
        String time = getTimeAgo(ts);
        tvAchievementDateTime.setText("Achieved "+time);
    }

    public void setTimestamp (Timestamp ts){
        Date date = new Date(ts.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM'.' d, yyyy 'at' hh:mm aaa");
        tvAchievementDateTime.setText(sdf.format(date));
    }

    public void setPats(int i){
        tvAchievementPats.setText(String.valueOf(i));
    }

    public void setMehs(int i){
        tvAchievementMehs.setText(String.valueOf(i));
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
