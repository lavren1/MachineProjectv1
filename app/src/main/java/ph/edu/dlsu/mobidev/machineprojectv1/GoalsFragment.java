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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_view_goals, container, false);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance().getReference();

        if(mAuth.getCurrentUser() == null){ //no user logged in
            startActivity(new Intent(getContext().getApplicationContext(), LoginActivity.class));
        }

        rvGoals = view.findViewById(R.id.rv_goals);
        rvGoals.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        fabAddGoal = view.findViewById(R.id.fab_add_goal);
        fabAddGoal.setImageResource(R.drawable.ic_add_black_24dp);
        fabAddGoal.setOnClickListener(this);


        //firebase recycler view
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("goals");
        FirebaseRecyclerAdapter<Goal, GoalHolder>adapter = new FirebaseRecyclerAdapter<Goal, GoalHolder>(Goal.class, R.layout.item_goal, GoalHolder.class, ref){

            @Override
            protected void populateViewHolder(GoalHolder viewHolder, Goal model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setTimestamp(model.getTimestamp());
                final String goalId = model.getGoalId();
                final Goal modelCopy = model;

                viewHolder.btnEditGoal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showEditGoalDialog(goalId);
                    }
                });
                viewHolder.btnDeleteGoal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteGoal(goalId);
                    }
                });
                viewHolder.btnAchieveGoal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        achieveGoal(modelCopy);
                    }

                });
            };
        };
        rvGoals.setAdapter(adapter);

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
    protected void showEditGoalDialog(final String goalId){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_goal_dialog, null))
                .setPositiveButton("Add Goal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog f = (Dialog) dialog;
                        EditText title, description;

                        title = f.findViewById(R.id.form_goal_title);
                        description  = f.findViewById(R.id.form_goal_desc);

                        String goalTitle = title.getText().toString().trim();
                        String goalDesc = description.getText().toString().trim();

                        editGoal(goalTitle, goalDesc, goalId);


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
                        EditText title, description;

                        title = f.findViewById(R.id.form_goal_title);
                        description  = f.findViewById(R.id.form_goal_desc);

                        String goalTitle = title.getText().toString().trim();
                        String goalDesc = description.getText().toString().trim();

                        createGoal(goalTitle, goalDesc);


                    }
                })
                .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    public void saveGoal(FirebaseUser user, final String title, final String desc){
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
                Goal goal = new Goal(title, desc, timestamp, username, goalKey);
                userGoalRef.setValue(goal);

                Log.d("Test", username + ": new goal added");

                showSnackbar("Added Goal");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
    public void showSnackbar(String message){
        //snackbar
        View view = getActivity().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        View snackView = snackbar.getView();
        //TextView textView = snackView.findViewById(android.support.design.R.id.snackbar_text);

        snackView.setBackgroundColor(Color.parseColor("#3F51B5"));

        Snackbar.make(view, "Goal Added", Snackbar.LENGTH_LONG).show();
    }

    public void createGoal(String title, String desc){

        FirebaseUser user = mAuth.getCurrentUser();

        if(title.isEmpty()){
            //todo cancel
        }
        else
        saveGoal(user, title, desc);

    }

    public void editGoal(String title, String desc, String goalId){
        if(title.isEmpty()){
            //todo cancel
        }
        else{
        FirebaseUser user = mAuth.getCurrentUser();
        Map<String, Object> goalUpdates = new HashMap<>();
        goalUpdates.put("description", desc);
        goalUpdates.put("title", title);
        DatabaseReference goalRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getUid()).child("goals").child(goalId);
        goalRef.updateChildren(goalUpdates);
        showSnackbar("Edited Goal");
       }
    }

    protected void deleteGoal(String goalID){
        FirebaseUser cUser = mAuth.getCurrentUser();
        FirebaseDatabase ref = FirebaseDatabase.getInstance();
        DatabaseReference glRef = FirebaseDatabase.getInstance().getReference("users").child(cUser.getUid()).child("goals").child(goalID);
        glRef.removeValue();

        showSnackbar("Removed goal.");
    }
    
    protected void achieveGoal (Goal model) {
        Achievement newModel = new Achievement();
        ph.edu.dlsu.mobidev.machineprojectv1.Timestamp ts = new ph.edu.dlsu.mobidev.machineprojectv1.Timestamp(System.currentTimeMillis());

        newModel.setTitle(model.getTitle());
        newModel.setDescription(model.getDescription());
        newModel.setTimestamp(ts);
        newModel.setUsername(model.getUsername());
        newModel.setAchievementId(model.getGoalId());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference achvmntRef = FirebaseDatabase.getInstance().getReference("achievements").child(newModel.getAchievementId());
        achvmntRef.setValue(newModel);
        DatabaseReference userAchvmntRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("achievements").child(newModel.getAchievementId());
        userAchvmntRef.setValue(newModel);

        DatabaseReference goalRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("goals").child(model.getGoalId());
        goalRef.removeValue();

        showSnackbar("Goal achieved!");
    }

}
