package ph.edu.dlsu.mobidev.machineprojectv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;

/**
 * Created by Noel Campos on 11/12/2017.
 */

public class GoalActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    EditText etGoalTitle, etGoalDesc;
    Button btnAddGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

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

    public void createGoal(){
        FirebaseUser user = mAuth.getCurrentUser();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String title = etGoalTitle.getText().toString().trim();
        String description = etGoalDesc.getText().toString().trim();

        if(title.isEmpty()){
            etGoalTitle.setError("Title is required");
            etGoalTitle.requestFocus();
            return;
        }

        Goal goal = new Goal(title, description, timestamp);

        mDatabase.child(user.getUid()).child("goals").setValue(goal);

        //todo change toast
        Toast.makeText(this, "Goal Added!", Toast.LENGTH_LONG).show();

    }

    public void showGoals(){

    }
}
