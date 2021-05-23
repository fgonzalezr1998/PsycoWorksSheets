package com.example.psycoworksheets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class SuccessfullySignedDialogFragment extends DialogFragment {

    private String userName;
    private TextView userNameTextView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getArgs();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.successfully_signed_dialog_fragment, null));
        builder.setPositiveButton("OK", (dialog, id) -> {
            // Do something ...
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        userNameTextView = (TextView) getDialog().findViewById(R.id.editTextUserName);
        userNameTextView.setText(userName);
    }

    private void getArgs() {
        userName = getArguments().getString("user_name");
    }
}
