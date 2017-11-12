package com.example.mobidev.machineproject;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Nikko on 11/11/2017.
 */

public class AchievementsFragment extends Fragment{
    private static final String TAG = "FeedsFragment";

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.achievements, container, false);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }
}
