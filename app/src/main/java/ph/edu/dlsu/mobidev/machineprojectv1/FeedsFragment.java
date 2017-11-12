package ph.edu.dlsu.mobidev.machineprojectv1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Nikko on 11/11/2017.
 */

public class FeedsFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "FeedsFragment";

    private FirebaseAuth mAuth;
    Button btnAddGoal, btnLogOut, btnDelete;
    TextView tvUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        btnLogOut = (Button) view.findViewById(R.id.btn_log_out);
        btnAddGoal = (Button) view.findViewById(R.id.btn_to_add_goal);
        btnDelete = (Button) view.findViewById(R.id.btn_delete_all);
        tvUser = (TextView) view.findViewById(R.id.tv_user);


        tvUser.setText("Hello "+user.getEmail());
        btnLogOut.setOnClickListener(this);
        btnAddGoal.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        return view;
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
                startActivity(new Intent(getContext().getApplicationContext(), LoginActivity.class));
                break;
            case R.id.btn_delete_all:
                deleteAllData();
                break;
        }

    }
}
