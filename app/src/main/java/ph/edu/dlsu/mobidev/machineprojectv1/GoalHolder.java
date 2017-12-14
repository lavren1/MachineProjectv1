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
 * Created by Noel Campos on 11/12/2017.
 */

public class GoalHolder extends ViewHolder{
    private final TextView tvGoalTitle;
    private final  TextView tvGoalDesc;
    private final TextView tvGoalDateTime;
    public final Button btnEditGoal;
    public final Button btnDeleteGoal;
    public final Button btnAchieveGoal;

    public GoalHolder(View itemView) {
        super(itemView);

        tvGoalTitle = itemView.findViewById(R.id.tv_goal_title);
        tvGoalDesc = itemView.findViewById(R.id.tv_goal_desc);
        tvGoalDateTime = itemView.findViewById(R.id.tv_goal_ts);
        btnEditGoal = itemView.findViewById(R.id.btn_edit_goal);
        btnDeleteGoal = itemView.findViewById(R.id.btn_delete_goal);
        btnAchieveGoal = itemView.findViewById(R.id.btn_achieve_goal);

    }

    public void setTitle (String t){
        tvGoalTitle.setText(t);

    }

    public void setDesc (String t){
        tvGoalDesc.setText(t);

    }

    public void setTimestamp (Timestamp ts){
        Date date = new Date(ts.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvGoalDateTime.setText(sdf.format(date));
    }
}
