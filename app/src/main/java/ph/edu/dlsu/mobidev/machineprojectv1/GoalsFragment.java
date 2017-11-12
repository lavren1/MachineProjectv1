package ph.edu.dlsu.mobidev.machineprojectv1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikko on 11/11/2017.
 */

public class GoalsFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "FeedsFragment";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mFirebaseDB;
    private EditText etGoalTitle, etGoalDesc;
    private Button btnAddGoal;
    private RecyclerView rvGoals;

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

        btnAddGoal = view.findViewById(R.id.btn_to_add_goal);

        btnAddGoal.setOnClickListener(this);

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
            case R.id.btn_add_goal:
                createGoal();
                break;
        }
    }

    public void saveGoal(FirebaseUser user){
        final String title = etGoalTitle.getText().toString().trim();
        final String description = etGoalDesc.getText().toString().trim();
        final ph.edu.dlsu.mobidev.machineprojectv1.Timestamp timestamp = new ph.edu.dlsu.mobidev.machineprojectv1.Timestamp(System.currentTimeMillis());

        FirebaseDatabase ref = FirebaseDatabase.getInstance();

        final DatabaseReference userRef = ref.getReference("users").child(user.getUid()).child("username");
        final List<User> tempList = new ArrayList<>();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);

                DatabaseReference goalsRef = mFirebaseDB.child("goals");
                DatabaseReference newGoalRef = goalsRef.push();

                FirebaseUser user = mAuth.getCurrentUser();

                String goalKey = newGoalRef.getKey();
                Goal goal = new Goal(title, description, timestamp, username, goalKey);

                newGoalRef.setValue(new Goal(title, description, timestamp, username));

                mFirebaseDB.child("users").child(user.getUid()).child("goals").push().setValue(goal);

                Log.d("Test", username + ": new goal added");

                //todo change toast
                Toast.makeText(getContext().getApplicationContext(), "Goal Added!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public void createGoal(){
        FirebaseUser user = mAuth.getCurrentUser();
        String title = etGoalTitle.getText().toString().trim();

        if(title.isEmpty()){
            etGoalTitle.setError("Title is required");
            etGoalTitle.requestFocus();
            return;
        }

        saveGoal(user);

    }

    public void showGoals(){
        //show goals

    }
}
