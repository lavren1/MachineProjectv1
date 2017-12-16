package ph.edu.dlsu.mobidev.machineprojectv1;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikko on 11/11/2017.
 */

public class GoalsFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "FeedsFragment";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mFirebaseDB;
    private RecyclerView rvGoals;
    private FloatingActionButton fabAddGoal;
    private TextView tvGoalsBlankState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_view_goals, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance().getReference();

        tvGoalsBlankState = (TextView) view.findViewById(R.id.tv_goals_blank_state);
        
        if(mAuth.getCurrentUser() == null){ //no user logged in
            startActivity(new Intent(getContext().getApplicationContext(), LoginActivity.class));
        }

        rvGoals = view.findViewById(R.id.rv_goals);
        rvGoals.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        fabAddGoal = view.findViewById(R.id.fab_add_goal);
        fabAddGoal.setImageResource(R.drawable.addicon);
        fabAddGoal.setOnClickListener(this);

        //firebase recycler view
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("goals");
        FirebaseRecyclerAdapter<Goal, GoalHolder>adapter = new FirebaseRecyclerAdapter<Goal, GoalHolder>
                (Goal.class, R.layout.item_goal, GoalHolder.class, ref.orderByChild("timestamps")){

            @Override
            protected void populateViewHolder(final GoalHolder viewHolder, Goal model, int position) {
                viewHolder.setDesc(model.getDescription());
                viewHolder.setTimestamp(model.getTimestamp());
                final String goalId = model.getGoalId();
                final Goal modelCopy = model;
                final String goalDesc = model.getDescription();

                viewHolder.tvGoalOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(getActivity(), viewHolder.tvGoalOptions);
                        popupMenu.inflate(R.menu.goal_options_menu);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch(item.getItemId()){
                                    case R.id.edit_goal:
                                        showEditGoalDialog(goalId, goalDesc);
                                        break;
                                    case R.id.delete_goal:
                                        confirmDelete(goalId);
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });

                viewHolder.btnAchieveGoal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmAchieve(modelCopy);
                    }

                });
            };
        };
        rvGoals.setAdapter(adapter);
        
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()) {
                    tvGoalsBlankState.setText("You don't have any goals yet, click on the bottom right button and get started!");
                } else {
                    tvGoalsBlankState.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_goal:
                showAddGoalDialog();
                break;
        }
    }
    protected void showEditGoalDialog(final String goalId, final String oldGoalDesc){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_goal_dialog, null))
                .setPositiveButton("Edit Goal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog f = (Dialog) dialog;
                        EditText description;

                        description = f.findViewById(R.id.form_goal_desc);
                        String goalDesc = description.getText().toString().trim();

                        editGoal(goalDesc, goalId, goalDesc);
                    }
                })
                .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    protected void showAddGoalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_goal_dialog, null))
                // Add action buttons
                .setPositiveButton("Add Goal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog f = (Dialog) dialog;
                        EditText description;

                        description  = f.findViewById(R.id.form_goal_desc);

                        String goalDesc = description.getText().toString().trim();

                        createGoal(goalDesc);
                    }
                })
                .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    public void saveGoal(FirebaseUser user, final String desc){
        final ph.edu.dlsu.mobidev.machineprojectv1.Timestamp timestamp
                = new ph.edu.dlsu.mobidev.machineprojectv1.Timestamp(System.currentTimeMillis());

        FirebaseDatabase ref = FirebaseDatabase.getInstance();

        final DatabaseReference userRef = ref.getReference("users").child(user.getUid()).child("username");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);

                FirebaseUser user = mAuth.getCurrentUser();

                DatabaseReference userGoalRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("goals").push();
                String goalKey = userGoalRef.getKey();
                Goal goal = new Goal(desc, timestamp, username, goalKey);
                goal.setTimestamps(-1 * new Date().getTime());
                userGoalRef.setValue(goal);

                Log.d("Test", username + ": new goal added");

                showSnackbar("Goal Added!");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
    public void showSnackbar(String message){
        View view = getActivity().findViewById(android.R.id.content);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public void createGoal(String desc){

        FirebaseUser user = mAuth.getCurrentUser();

        if(desc.isEmpty()){
            Toast.makeText(getActivity(), "Description cannot be empty!", Toast.LENGTH_SHORT).show();
            showAddGoalDialog();
        }
        else
        saveGoal(user, desc);

    }

    public void editGoal(String desc, String goalId, String goalDesc){
        if(desc.isEmpty()){
            Toast.makeText(getActivity(), "Description cannot be empty!", Toast.LENGTH_SHORT).show();
            showEditGoalDialog(goalId, goalDesc);
        }
        else{
        FirebaseUser user = mAuth.getCurrentUser();
        Map<String, Object> goalUpdates = new HashMap<>();
        goalUpdates.put("description", desc);
        DatabaseReference goalRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getUid()).child("goals").child(goalId);
        goalRef.updateChildren(goalUpdates);
        showSnackbar("Goal Edited!");
       }
    }

    protected void deleteGoal(String goalID){
        FirebaseUser cUser = mAuth.getCurrentUser();
        FirebaseDatabase ref = FirebaseDatabase.getInstance();
        DatabaseReference glRef = FirebaseDatabase.getInstance().getReference("users").child(cUser.getUid()).child("goals").child(goalID);
        glRef.removeValue();

        showSnackbar("Goal Deleted!");
    }
    
    protected void achieveGoal (Goal model) {
        Achievement newModel = new Achievement();
        ph.edu.dlsu.mobidev.machineprojectv1.Timestamp ts = new ph.edu.dlsu.mobidev.machineprojectv1.Timestamp(System.currentTimeMillis());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        newModel.setDescription(model.getDescription());
        newModel.setTimestamp(ts);
        newModel.setUsername(model.getUsername());
        newModel.setAchievementId(model.getGoalId());
        newModel.setTimestamps(-1 * new Date().getTime());
        newModel.setUsernameKey(user.getUid());

        Map<String, Object>achievementValues = newModel.toMap();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference achvmntRef = FirebaseDatabase.getInstance().getReference("activity_view_achievements")
                .child(newModel.getAchievementId());

        achvmntRef.setValue(achievementValues);

        DatabaseReference userAchvmntRef = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUser.getUid())
                .child("activity_view_achievements")
                .child(newModel.getAchievementId());
        userAchvmntRef.setValue(achievementValues);

        DatabaseReference goalRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("goals").child(model.getGoalId());
        goalRef.removeValue();

        showSnackbar("Goal Achieved!");
    }

    protected void confirmAchieve (final Goal model){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Confirm Achieve Goal")
                .setMessage("Are you sure you have achieved this?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        achieveGoal(model);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    protected void confirmDelete (final String goalID){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Confirm Delete Goal")
                .setMessage("Are you sure you want to delete this?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteGoal(goalID);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    
}
