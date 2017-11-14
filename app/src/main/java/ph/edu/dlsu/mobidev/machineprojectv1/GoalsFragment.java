package ph.edu.dlsu.mobidev.machineprojectv1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        fabAddGoal.setOnClickListener(this);

        //firebase recycler view
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("goals");
        FirebaseRecyclerAdapter<Goal, GoalHolder>adapter = new FirebaseRecyclerAdapter<Goal, GoalHolder>(Goal.class, R.layout.item_goal, GoalHolder.class, ref){

            @Override
            protected void populateViewHolder(GoalHolder viewHolder, Goal model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setTimestamp(model.getTimestamp());
            }
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
        //todo dialog na nga lang
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

                DatabaseReference goalsRef = FirebaseDatabase.getInstance().getReference().child("goals");
                DatabaseReference newGoalRef = goalsRef.push();

                FirebaseUser user = mAuth.getCurrentUser();

                String goalKey = newGoalRef.getKey();
                Goal goal = new Goal(title, desc, timestamp, username, goalKey);

                newGoalRef.setValue(new Goal(title, desc, timestamp, username));

                FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("goals").push().setValue(goal);

                Log.d("Test", username + ": new goal added");

                //todo change toast
                Toast.makeText(getActivity(), "Goal Added!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public void createGoal(String title, String desc){

        FirebaseUser user = mAuth.getCurrentUser();

        if(title.isEmpty()){
            //todo cancel
        }
        else
        saveGoal(user, title, desc);

    }

}
