package com.example.psycoworksheets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SigninDialogFragment extends DialogFragment {

    private static final int MIN_PSSWD = 6;

    private TextView signinName, signinSurname, signinEmail, signinPassword;
    private String errorMsg = "", table;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private SignInFragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (SignInFragmentListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getArgs();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.signin_dialog_fragment, null));
        builder.setPositiveButton("OK", (dialog, id) -> {
            // sign in the user ...
            signIn();
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> {
            getDialog().cancel();
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        signinName = (TextView) getDialog().findViewById(R.id.signinName);
        signinSurname = (TextView) getDialog().findViewById(R.id.signinSurname);
        signinEmail = (TextView) getDialog().findViewById(R.id.signinEmail);
        signinPassword = (TextView) getDialog().findViewById(R.id.signinPassword);
    }

    public interface SignInFragmentListener {
        void runSuccessfullySigned(String userName);
    }

    private boolean fieldsOk() {
        boolean ok = true;
        if (emptyFields()) {
            errorMsg = "Error! Empty Fields";
            ok = false;
        } else if (!emailOK()) {
            errorMsg = "Error! Invalid Email";
            ok = false;
        } else if (!psswdOk()) {
            errorMsg = "Error! Password must have more than 6 characters";
            ok = false;
        }

        return ok;
    }

    private boolean psswdOk() {
        String password;

        password = signinPassword.getText().toString();

        return password.length() >= MIN_PSSWD;
    }

    private boolean emailOK() {
        String email;
        int posArroba, posPoint;

        email = signinEmail.getText().toString();

        posArroba = email.indexOf('@');
        if (posArroba <= 1)
            return false;

        posPoint = email.lastIndexOf('.');

        if ((posPoint < 0) || (posPoint == email.length() - 1) || (posPoint < posArroba))
            return false;

        return posPoint - posArroba > 1;
    }

    private boolean emptyFields() {
        return signinName.getText().toString().isEmpty() ||
                signinSurname.getText().toString().isEmpty() ||
                signinEmail.getText().toString().isEmpty() ||
                signinPassword.getText().toString().isEmpty();
    }

    private void signIn() {
        Map<String, Object> map = new HashMap<>();
        AtomicReference<String> id = new AtomicReference<>("");

        if (fieldsOk()) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(signinEmail.getText().toString(),
                signinPassword.getText().toString()).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        id.set(mAuth.getCurrentUser().getUid());
                        map.put("name", signinName.getText().toString());
                        map.put("surname", signinSurname.getText().toString());
                        map.put("email", signinEmail.getText().toString());

                        mDatabase.child("Users").child(table).child(id.get()).setValue(map);
                    }
                });
            fragmentListener.runSuccessfullySigned(signinName.getText().toString());
        } else {
            reportError();
        }
    }

    private void getArgs() {
        table = getArguments().getString("table");
    }

    private void reportError() {
        Log.w("App", errorMsg);
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }
}
