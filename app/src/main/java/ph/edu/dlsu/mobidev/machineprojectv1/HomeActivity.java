package ph.edu.dlsu.mobidev.machineprojectv1;

import android.support.design.widget.TabLayout;
import android.util.Log;
import android.support.v4.view.ViewPager;
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
    private static final String TAG = "MainActivity";
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate: Starting.");

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if(mAuth.getCurrentUser() == null){ //no user logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

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
            case R.id.btn_delete_all:
                deleteAllData();
        }

    }
    
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FeedsFragment(), "Feed");
        adapter.addFragment(new GoalsFragment(), "Goals");
        adapter.addFragment(new AchievementsFragment(), "Achievement");
        viewPager.setAdapter(adapter);
    }
    
    public void createAchievement(EditText aTitle, EditText aDesc){

        etAchieveTitle = aTitle;
        etAchieveDescription = aDesc;

        FirebaseUser user = mAuth.getCurrentUser();
        final String title = etAchieveTitle.getText().toString();
        final String description = etAchieveDescription.getText().toString();
        final com.example.mobidev.machineproject.Timestamp timestamp = new com.example.mobidev.machineproject.Timestamp(System.currentTimeMillis());

        FirebaseDatabase ref = FirebaseDatabase.getInstance();
        DatabaseReference userRef = ref.getReference("users").child(user.getUid()).child("username");

        if(title.isEmpty()){
            etAchieveTitle.setError("Title is required");
            etAchieveTitle.requestFocus();
            return;
        }

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets username
                String username = dataSnapshot.getValue(String.class);

                //makes the achievements child directly under the root
                DatabaseReference achievementsRef = mDatabase.getReference().child("achievements");
                //instantiates an achievement with an ID via push
                DatabaseReference newAchievementsRef = achievementsRef.push();

                FirebaseUser user = mAuth.getCurrentUser();

                //gets the unique generated ID of the achievement
                String achievementKey = newAchievementsRef.getKey();
                Achievement achievement = new Achievement(title, description, timestamp, username, achievementKey);

                //sets the value of the achievement under the root
                newAchievementsRef.setValue(new Achievement(title, description, timestamp, username));

                //under users naman to
                mDatabase.getReference().child("users").child(user.getUid()).child("achievements").child(achievementKey).setValue(achievement);

                Toast.makeText(getApplicationContext(), "Achievement Added!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
