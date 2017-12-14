package ph.edu.dlsu.mobidev.machineprojectv1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

/**
 * Created by Nikko on 11/11/2017.
 */

public class AchievementsFragment extends Fragment {
    private static final String TAG = "AchievementsFragment";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mFirebaseDB;
    private RecyclerView rvAchievements;
    private FloatingActionButton fabAddAchievement;
    EditText etAchieveTitle, etAchieveDescription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_view_achievements, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance().getReference("activity_view_achievements");

        rvAchievements = view.findViewById(R.id.self_achievements);
        rvAchievements.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        etAchieveTitle = (EditText) view.findViewById(R.id.form_achTitle);
        etAchieveDescription = (EditText) view.findViewById(R.id.form_achDesc);
        fabAddAchievement = (FloatingActionButton) view.findViewById(R.id.fab_add_achievement);
        fabAddAchievement.setImageResource(R.drawable.ic_add_black_24dp);

        //firebase recycler view
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("activity_view_achievements");
        FirebaseRecyclerAdapter<Achievement, AchievementHolder>achvmntAdapter = new FirebaseRecyclerAdapter<Achievement, AchievementHolder>(Achievement.class, R.layout.item_achievement, AchievementHolder.class, ref){
            @Override
            protected void populateViewHolder(AchievementHolder viewHolder, Achievement model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setTimestamp(model.getTimestamp());
                final String achievementID = model.getAchievementId();

                viewHolder.btnDeleteAchievement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteAchievement(achievementID);
                    }
                });
            }
        };
        rvAchievements.setAdapter(achvmntAdapter);

        fabAddAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAchievementDialog dialog1 = new AddAchievementDialog();
                dialog1.show(getFragmentManager(), "addachievement");
            }
        });

        return view;
    }

    protected void deleteAchievement(String achievementID){
        FirebaseUser cUser = mAuth.getCurrentUser();
        FirebaseDatabase ref = FirebaseDatabase.getInstance();
        DatabaseReference achvmntRef = FirebaseDatabase.getInstance().getReference("activity_view_achievements").child(achievementID);
        DatabaseReference userAchvmntRef = FirebaseDatabase.getInstance().getReference("users").child(cUser.getUid()).child("activity_view_achievements").child(achievementID);
        achvmntRef.removeValue();
        userAchvmntRef.removeValue();
        
        showSnackbar("Deleted Achievement.");
    }
    
    public void showSnackbar(String message){
        //snackbar
        View view = getActivity().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(Color.parseColor("#3F51B5"));
        //TextView textView = snackView.findViewById(android.support.design.R.id.snackbar_text);
        snackbar.show();

        //Snackbar.make(view, "Goal Added", Snackbar.LENGTH_LONG).show();
    }

    private void onReactClick(DatabaseReference achRef){
        achRef.runTransaction(new Transaction.Handler() {


        });
    }
}
