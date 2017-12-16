package ph.edu.dlsu.mobidev.machineprojectv1;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.Button;
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
    public final Button btnDeleteAchievement;
    public final TextView tvAchievementPats;
    public final TextView tvAchievementMehs;

    public AchievementHolder(View itemView) {
        super(itemView);

        tvAchievementDesc = itemView.findViewById(R.id.tv_achievement_desc);
        tvAchievementDateTime = itemView.findViewById(R.id.tv_achievement_ts);
        btnDeleteAchievement = itemView.findViewById(R.id.btn_delete_achievement);
        tvAchievementPats = itemView.findViewById(R.id.tv_ach_pat_count);
        tvAchievementMehs = itemView.findViewById(R.id.tv_ach_meh_count);
    }


    public void setDesc (String t){
        tvAchievementDesc.setText(t);
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
}
