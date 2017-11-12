package ph.edu.dlsu.mobidev.machineprojectv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noel Campos on 11/12/2017.
 */

public class GoalActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    EditText etGoalTitle, etGoalDesc;
    Button btnAddGoal;
    //List<User> tempList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(mAuth.getCurrentUser() == null){ //no user logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        etGoalTitle = (EditText) findViewById(R.id.et_goal_title);
        etGoalDesc = (EditText) findViewById(R.id.et_goal_description);
        btnAddGoal = (Button) findViewById(R.id.btn_add_goal);

        btnAddGoal.setOnClickListener(this);


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
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        FirebaseDatabase ref = FirebaseDatabase.getInstance();

        final DatabaseReference userRef = ref.getReference("users").child(user.getUid()).child("username");
        final List<User> tempList = new ArrayList<>();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);

                DatabaseReference goalsRef = mDatabase.child("goals");
                DatabaseReference newGoalRef = goalsRef.push();

                FirebaseUser user = mAuth.getCurrentUser();

                String goalKey = newGoalRef.getKey();
                Key key = new Key(goalKey);

                newGoalRef.setValue(new Goal(title, description, timestamp, username));

                mDatabase.child("users").child(user.getUid()).child("goals").setValue(key);

                //todo change toast
                Toast.makeText(getApplicationContext(), "Goal Added!", Toast.LENGTH_SHORT).show();

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
