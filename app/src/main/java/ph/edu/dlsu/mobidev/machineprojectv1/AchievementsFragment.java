package ph.edu.dlsu.mobidev.machineprojectv1;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class AchievementsFragment extends Fragment {
    private static final String TAG = "AchievementsFragment";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mFirebaseDB;
    private RecyclerView rvAchievements;
    private FloatingActionButton fabAddAchievement;
    EditText etAchieveTitle, etAchieveDescription;
    private TextView tvAchievementsBlankState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        final View view = inflater.inflate(R.layout.activity_view_achievements, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance().getReference("achievements");

        tvAchievementsBlankState = (TextView) view.findViewById(R.id.tv_achievements_blank_state);
        
        rvAchievements = view.findViewById(R.id.self_achievements);
        rvAchievements.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        etAchieveDescription = (EditText) view.findViewById(R.id.form_achDesc);
        fabAddAchievement = (FloatingActionButton) view.findViewById(R.id.fab_add_achievement);
        fabAddAchievement.setImageResource(R.drawable.addicon);

        //firebase recycler view
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users").child(user.getUid()).child("achievements");
        FirebaseRecyclerAdapter<Achievement, AchievementHolder>achvmntAdapter
                = new FirebaseRecyclerAdapter<Achievement, AchievementHolder>
                (Achievement.class, R.layout.item_achievement,
                        AchievementHolder.class, ref.orderByChild("timestamps")){
            @Override
            protected void populateViewHolder(AchievementHolder viewHolder, Achievement model, int position) {
                viewHolder.setDesc(model.getDescription());
                viewHolder.setDateAchieved(model.getTimestamps());
                viewHolder.setPats(model.getPatCount());
                viewHolder.setMehs(model.getMehCount());
                final String achievementID = getRef(position).getKey();
                final AchievementHolder achHolder = viewHolder;
                final DatabaseReference ref = getRef(position);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(model.pats.containsKey(user.getUid())){
                    viewHolder.tvAchievementPats.setTextColor(Color.parseColor("#ec5f54"));
                    viewHolder.tvAchievementPats.setTypeface(null, Typeface.BOLD);
                    viewHolder.setReactImageResource(1);
                } else{
                    viewHolder.tvAchievementPats.setTextColor(Color.GRAY);
                    viewHolder.tvAchievementPats.setTypeface(null, Typeface.NORMAL);
                    viewHolder.setReactImageResource(2);
                }
                if(model.mehs.containsKey(user.getUid())){
                    viewHolder.tvAchievementMehs.setTextColor(Color.parseColor("#ec5f54"));
                    viewHolder.tvAchievementMehs.setTypeface(null, Typeface.BOLD);
                    viewHolder.setReactImageResource(3);
                } else{
                    viewHolder.tvAchievementMehs.setTextColor(Color.GRAY);
                    viewHolder.tvAchievementMehs.setTypeface(null, Typeface.NORMAL);
                    viewHolder.setReactImageResource(4);
                }

                viewHolder.tvAchievementOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(getActivity(), achHolder.tvAchievementOptions);
                        popupMenu.inflate(R.menu.ach_options_menu);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch(item.getItemId()){
                                    case R.id.delete_achievement:
                                        confirmDelete(achievementID);
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });

                viewHolder.btnAchievementPat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onPatClicked(ref);
                    }
                });

                viewHolder.btnAchievementMeh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onMehClicked(ref);
                    }
                });
            }
        };
        rvAchievements.setAdapter(achvmntAdapter);
        
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()) {
                    tvAchievementsBlankState.setText("You currently don't have any achievements," +
                            " click on the bottom right button or work towards your current goals!");
                } else {
                    tvAchievementsBlankState.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        DatabaseReference achvmntRef = FirebaseDatabase.getInstance()
                .getReference("achievements").child(achievementID);
        DatabaseReference userAchvmntRef = FirebaseDatabase.getInstance()
                .getReference("users").child(cUser.getUid())
                .child("achievements").child(achievementID);
        achvmntRef.removeValue();
        userAchvmntRef.removeValue();
        
        showSnackbar("Achievement Deleted!");
    }
    
    protected void confirmDelete (final String achievementID){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Confirm Delete Achievement")
                .setMessage("Are you sure you want to delete this?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAchievement(achievementID);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    
    public void showSnackbar(String message){
        View view = getActivity().findViewById(android.R.id.content);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public void onPatClicked(final DatabaseReference achievementRef){
        achievementRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Achievement a = mutableData.getValue(Achievement.class);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference userAchRef = FirebaseDatabase.getInstance().getReference()
                        .child("achievements")
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
                        .child("achievements")
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
