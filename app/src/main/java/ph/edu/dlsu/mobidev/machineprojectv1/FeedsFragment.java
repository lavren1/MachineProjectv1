package ph.edu.dlsu.mobidev.machineprojectv1;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Nikko on 11/11/2017.
 */

public class FeedsFragment extends Fragment {
    private static final String TAG = "FeedsFragment";

    private FirebaseAuth mAuth;
    Button logoutbutton;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mFirebaseDB;
    private RecyclerView rvFeed;
    private TextView tvFeedBlankState;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_view_feed, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance().getReference("achievements");

        tvFeedBlankState = (TextView) view.findViewById(R.id.tv_feed_blank_state);
        
        rvFeed = (RecyclerView) view.findViewById(R.id.world_feed);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        progressBar = view.findViewById(R.id.feed_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        //firebase recycler view
        DatabaseReference allAchRef = FirebaseDatabase.getInstance().getReference("achievements");
        FirebaseRecyclerAdapter<Achievement, FeedHolder> feedAdapter = new FirebaseRecyclerAdapter<Achievement, FeedHolder>
                (Achievement.class, R.layout.item_feed, FeedHolder.class, allAchRef.orderByChild("timestamps")){
            @Override
            protected void populateViewHolder(final FeedHolder viewHolder, Achievement model, int position) {
                viewHolder.setOwner(model.getUsername());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setTimeAgo(model.getTimestamps());
                viewHolder.setPat(model.getPatCount());
                viewHolder.setMeh(model.getMehCount());
                final DatabaseReference ref = getRef(position);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(model.pats.containsKey(user.getUid())){
                    viewHolder.tvPatCount.setTextColor(Color.parseColor("#ec5f54"));
                    viewHolder.tvPatCount.setTypeface(null, Typeface.BOLD);
                    viewHolder.setReactImageResource(1);
                } else{
                    viewHolder.tvPatCount.setTextColor(Color.GRAY);
                    viewHolder.tvPatCount.setTypeface(null, Typeface.NORMAL);
                    viewHolder.setReactImageResource(2);
                }
                if(model.mehs.containsKey(user.getUid())){
                    viewHolder.tvMehCount.setTextColor(Color.parseColor("#ec5f54"));
                    viewHolder.tvMehCount.setTypeface(null, Typeface.BOLD);
                    viewHolder.setReactImageResource(3);
                } else{
                    viewHolder.tvMehCount.setTextColor(Color.GRAY);
                    viewHolder.tvMehCount.setTypeface(null, Typeface.NORMAL);
                    viewHolder.setReactImageResource(4);
                }

                viewHolder.btnPat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onPatClicked(ref);
                    }
                });

                viewHolder.btnMeh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onMehClicked(ref);
                    }
                });
            }
        };
        rvFeed.setAdapter(feedAdapter);
        progressBar.setVisibility(View.GONE);

        allAchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()){
                    tvFeedBlankState.setText("There hasn't been any achievements yet, work fast and be the first!");
                } else {
                    tvFeedBlankState.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
        return view;
    }

    public void onPatClicked(final DatabaseReference achievementRef){
        achievementRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Achievement a = mutableData.getValue(Achievement.class);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference userAchRef = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(a.usernameKey).child("achievements")
                        .child(achievementRef.getKey());
                Map<String, Object> achievementUpdates = new HashMap<>();
                int patcount, mehcount;
                if(a == null){
                    return Transaction.success(mutableData);
                }
                if(a.pats.containsKey(user.getUid())){
                    a.patCount--;
                    a.pats.remove(user.getUid());
                    patcount = a.patCount;
                    achievementUpdates.put("patCount", patcount);
                }
                else if(a.mehs.containsKey(user.getUid())){
                    a.mehCount--;
                    a.mehs.remove(user.getUid());
                    a.patCount++;
                    a.pats.put(user.getUid(), true);
                    patcount = a.patCount;
                    achievementUpdates.put("patCount", patcount);
                    mehcount = a.mehCount;
                    achievementUpdates.put("mehCount", mehcount);
                }
                else{
                    a.patCount++;
                    a.pats.put(user.getUid(), true);
                    patcount = a.patCount;
                    achievementUpdates.put("patCount", patcount);
                }
                achievementUpdates.put("pats", a.pats);
                achievementUpdates.put("mehs", a.mehs);
                userAchRef.updateChildren(achievementUpdates);
                mutableData.setValue(a);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                //Transaction completed
            }
        });
    }

    public void onMehClicked(final DatabaseReference achievementRef){
        achievementRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Achievement a = mutableData.getValue(Achievement.class);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference userAchRef = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(a.usernameKey).child("achievements")
                        .child(achievementRef.getKey());
                Map<String, Object> achievementUpdates = new HashMap<>();
                int patcount, mehcount;
                if(a == null){
                    return Transaction.success(mutableData);
                }
                if(a.mehs.containsKey(user.getUid())){
                    a.mehCount--;
                    a.mehs.remove(user.getUid());
                    mehcount = a.mehCount;
                    achievementUpdates.put("mehCount", mehcount);
                }
                else if(a.pats.containsKey(user.getUid())){
                    a.patCount--;
                    a.pats.remove(user.getUid());
                    a.mehCount++;
                    a.mehs.put(user.getUid(), true);
                    patcount = a.patCount;
                    mehcount = a.mehCount;
                    achievementUpdates.put("mehCount", mehcount);
                    achievementUpdates.put("patCount", patcount);
                }
                else{
                    a.mehCount++;
                    a.mehs.put(user.getUid(), true);
                    mehcount = a.mehCount;
                    achievementUpdates.put("mehCount", mehcount);
                }
                achievementUpdates.put("pats", a.pats);
                achievementUpdates.put("mehs", a.mehs);
                userAchRef.updateChildren(achievementUpdates);
                mutableData.setValue(a);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                //Transaction completed
            }
        });
    }
}
