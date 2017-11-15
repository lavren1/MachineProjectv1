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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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
        View view = inflater.inflate(R.layout.feed, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance().getReference("achievements");

        rvFeed = (RecyclerView) view.findViewById(R.id.world_feed);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        //firebase recycler view
        DatabaseReference allAchRef = FirebaseDatabase.getInstance().getReference("achievements");
        FirebaseRecyclerAdapter<Achievement, FeedHolder> feedAdapter = new FirebaseRecyclerAdapter<Achievement, FeedHolder>(Achievement.class, R.layout.feed_achievement_display, FeedHolder.class, allAchRef){
            @Override
            protected void populateViewHolder(FeedHolder viewHolder, Achievement model, int position) {
                viewHolder.setOwner(model.getUsername());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setTimestamp(model.getTimestamp());
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
}
