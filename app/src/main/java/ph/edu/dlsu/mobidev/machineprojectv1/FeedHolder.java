package ph.edu.dlsu.mobidev.machineprojectv1;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
}
