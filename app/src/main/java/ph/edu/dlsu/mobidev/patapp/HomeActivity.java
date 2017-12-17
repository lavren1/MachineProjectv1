package ph.edu.dlsu.mobidev.patapp;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Map;


/**
 * Created by Noel Campos on 11/11/2017.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    private ViewPager mViewPager;
    private ImageView ivLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if(mAuth.getCurrentUser() == null){ //no user logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        
        ivLogout = (ImageView) findViewById(R.id.btn_logoutMain);
        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent gobacktosquareone = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(gobacktosquareone);
                finish();
            }
        });

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
            case R.id.btn_delete_all:
                deleteAllData();
        }

    }
    
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FeedsFragment(), "Feed");
        adapter.addFragment(new AchievementsFragment(), "Achievement");
        adapter.addFragment(new GoalsFragment(), "Goals");
        viewPager.setAdapter(adapter);
    }
    
    public void createAchievement(EditText aDesc){

        EditText etAchieveDescription = aDesc;

        FirebaseUser user = mAuth.getCurrentUser();
        final String description = etAchieveDescription.getText().toString();
        final ph.edu.dlsu.mobidev.patapp.Timestamp timestamp = new ph.edu.dlsu.mobidev.patapp.Timestamp(System.currentTimeMillis());

        FirebaseDatabase ref = FirebaseDatabase.getInstance();
        DatabaseReference userRef = ref.getReference("users").child(user.getUid()).child("username");

        if(description.isEmpty()){
            etAchieveDescription.setError("Title is required");
            etAchieveDescription.requestFocus();
            return;
        }
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets username
                String username = dataSnapshot.getValue(String.class);

                //makes the activity_view_achievements child directly under the root
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference achievementsRef = mDatabase.getReference().child("achievements");
                //instantiates an achievement with an ID via push
                DatabaseReference newAchievementsRef = achievementsRef.push();

                FirebaseUser user = mAuth.getCurrentUser();

                //gets the unique generated ID of the achievement
                String achievementKey = newAchievementsRef.getKey();
                Achievement achievement = new Achievement(description, timestamp, username, achievementKey);
                achievement.setTimestamps(-1 * new Date().getTime());
                achievement.setUsernameKey(user.getUid());
                Map<String, Object> achievementValues = achievement.toMap();
                //sets the value of the achievement under the root
                newAchievementsRef.setValue(achievementValues);

                //under users naman to
                mDatabase.getReference().child("users").child(user.getUid()).child("achievements").child(achievementKey).setValue(achievement);

                //Toast.makeText(getApplicationContext(), "Achievement Added!", Toast.LENGTH_SHORT).show();
                showSnackbar("Achievement Added!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showSnackbar(String message){
        View view = findViewById(android.R.id.content);

        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

}
