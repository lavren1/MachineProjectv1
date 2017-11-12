package ph.edu.dlsu.mobidev.machineprojectv1;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import com.github.fabtransitionactivity.SheetLayout;
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
    private RecyclerView rvGoals;
    private FloatingActionButton fabAddGoal;
    private SheetLayout mSheetLayout;

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
                mSheetLayout.expandFab();
                break;
        }
        //todo dialog na nga lang
    }

}
