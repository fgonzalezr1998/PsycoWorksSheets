package com.example.psycoworksheets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;

public class LogInDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.login_dialog_fragment, null));
        builder.setPositiveButton("OK", (dialog, id) -> {
            // sign in the user ...
            Log.w("App", "Log In in the User!");
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> {
            LogInDialogFragment.this.getDialog().cancel();
        });

        return builder.create();
    }
}
