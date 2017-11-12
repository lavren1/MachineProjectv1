package com.example.mobidev.machineproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by Nikko on 11/11/2017.
 */

public class FeedsFragment extends Fragment{
    private static final String TAG = "FeedsFragment";

    private FirebaseAuth mAuth;
    Button logoutbutton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.feed, container, false);

        mAuth = FirebaseAuth.getInstance();

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
