package ph.edu.dlsu.mobidev.machineprojectv1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Noel Campos on 11/12/2017.
 */

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {
    
    private List<Goal> goalItems;
    private Context context;

    public GoalAdapter(List<Goal> goalItems, Context context) {
        this.goalItems = goalItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goal, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvGoalTitle;
        public TextView tvGoalDesc;

        public ViewHolder(View itemView) {
            super(itemView);

            tvGoalTitle = itemView.findViewById(R.id.tv_goal_title);
            tvGoalDesc = itemView.findViewById(R.id.tv_goal_desc);
        }
    }
}
