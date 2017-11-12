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

public class HomeActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    private ViewPager mViewPager;
    private SectionsPageAdapter mSectionsPageAdapter;
    Button btnAddGoal, btnLogOut, btnDelete;
    TextView tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        
        if(mAuth.getCurrentUser() == null){ //no user logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
    
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FeedsFragment(), "Feed");
        adapter.addFragment(new GoalsFragment(), "Goals");
        adapter.addFragment(new AchievementsFragment(), "Achievement");
        viewPager.setAdapter(adapter);
    }

}
