package com.example.mobidev.machineproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Nikko on 11/13/2017.
 */

public class AddAchievementDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder1.setView(inflater.inflate(R.layout.add_achievement_dialog_layout, null))
                .setTitle("Add Achievement")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Dialog f = (Dialog) dialog;
                        EditText aTitle, aDesc;
                        aTitle = f.findViewById(R.id.form_achTitle);
                        aDesc = f.findViewById(R.id.form_achDesc);

                        ((MainActivity)getActivity()).createAchievement(aTitle, aDesc);
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

        return builder1.create();
    }
}
