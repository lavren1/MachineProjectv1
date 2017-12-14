package ph.edu.dlsu.mobidev.machineprojectv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

public class FeedsFragment extends Fragment {
    private static final String TAG = "FeedsFragment";

    private FirebaseAuth mAuth;
    Button logoutbutton;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mFirebaseDB;
    private RecyclerView rvFeed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_view_feed, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance().getReference("activity_view_achievements");

        rvFeed = (RecyclerView) view.findViewById(R.id.world_feed);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        //firebase recycler view
        DatabaseReference allAchRef = FirebaseDatabase.getInstance().getReference("activity_view_achievements");
        FirebaseRecyclerAdapter<Achievement, FeedHolder> feedAdapter = new FirebaseRecyclerAdapter<Achievement, FeedHolder>
                (Achievement.class, R.layout.item_feed, FeedHolder.class, allAchRef.orderByChild("timestamps")){
            @Override
            protected void populateViewHolder(FeedHolder viewHolder, Achievement model, int position) {
                viewHolder.setOwner(model.getUsername());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setTimestamp(model.getTimestamp());
                viewHolder.setPat(model.getPatCount());
                viewHolder.setMeh(model.getMehCount());
                final DatabaseReference ref = getRef(position);
                final String key = ref.getKey();
                final int fposition = position;

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

        logoutbutton = (Button) view.findViewById(R.id.logout_feed);
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent gobacktosquareone = new Intent(getActivity(), LoginActivity.class);
                startActivity(gobacktosquareone);
                getActivity().finish();
            }
        });

        return view;
    }

    public void onPatClicked(DatabaseReference achievementRef){
        achievementRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Achievement a = mutableData.getValue(Achievement.class);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(a == null){
                    return Transaction.success(mutableData);
                }
                if(a.pats.containsKey(user.getUid())){
                    a.patCount--;
                    a.pats.remove(user.getUid());
                }
                else if(a.mehs.containsKey(user.getUid())){
                    a.mehCount--;
                    a.mehs.remove(user.getUid());
                    a.patCount++;
                    a.pats.put(user.getUid(), true);
                }
                else{
                    a.patCount++;
                    a.pats.put(user.getUid(), true);
                }

                mutableData.setValue(a);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                //Transaction completed
            }
        });
    }

    public void onMehClicked(DatabaseReference achievementRef){
        achievementRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Achievement a = mutableData.getValue(Achievement.class);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(a == null){
                    return Transaction.success(mutableData);
                }
                if(a.mehs.containsKey(user.getUid())){
                    a.mehCount--;
                    a.mehs.remove(user.getUid());
                }
                else if(a.pats.containsKey(user.getUid())){
                    a.patCount--;
                    a.pats.remove(user.getUid());
                    a.mehCount++;
                    a.mehs.put(user.getUid(), true);
                }
                else{
                    a.mehCount++;
                    a.mehs.put(user.getUid(), true);
                }

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
