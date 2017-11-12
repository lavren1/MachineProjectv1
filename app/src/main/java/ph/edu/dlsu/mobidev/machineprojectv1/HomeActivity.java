package ph.edu.dlsu.mobidev.machineprojectv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Noel Campos on 11/11/2017.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;

    Button btnAddGoal, btnLogOut, btnDelete;
    TextView tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){ //no user logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();

        btnLogOut = (Button) findViewById(R.id.btn_log_out);
        btnAddGoal = (Button) findViewById(R.id.btn_to_add_goal);
        btnDelete = (Button) findViewById(R.id.btn_delete_all);
        tvUser = (TextView) findViewById(R.id.tv_user);


        tvUser.setText("Hello "+user.getEmail());
        btnLogOut.setOnClickListener(this);
        btnAddGoal.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

    }
    public void deleteAllData(){ //for dev
        DatabaseReference usernode = FirebaseDatabase.getInstance().getReference().getRoot().child("users");
        DatabaseReference goalnode = FirebaseDatabase.getInstance().getReference().getRoot().child("goals");

        usernode.setValue(null);
        goalnode.setValue(null);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_log_out:
                mAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btn_to_add_goal:
                finish();
                startActivity(new Intent(this, GoalActivity.class));
                break;
            case R.id.btn_delete_all:
                deleteAllData();
        }

    }

}
