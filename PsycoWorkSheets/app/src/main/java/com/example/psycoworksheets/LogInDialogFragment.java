package com.example.psycoworksheets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInDialogFragment extends DialogFragment {

    private EditText userMail, userPsswd;
    private FirebaseAuth mAuth;
    private Context context;

    private LogInFragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
        fragmentListener = (LogInFragmentListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.login_dialog_fragment, null));
        builder.setPositiveButton("OK", (dialog, id) -> {
            // log in the user ...
            Log.w("App", "Log In the User!");
            logIn();
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> {
            LogInDialogFragment.this.getDialog().cancel();
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        userMail = (EditText) getDialog().findViewById(R.id.editTextemail);
        userPsswd = (EditText) getDialog().findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    private void logIn() {
        String mail, psswd;

        mail = userMail.getText().toString();
        psswd = userPsswd.getText().toString();

        mAuth.signInWithEmailAndPassword(mail, psswd)
            .addOnCompleteListener(task ->  {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.w("LogInDialog", "signInWithEmail:success");
                    FirebaseUser user = null;
                    user = mAuth.getCurrentUser();
                    if (user == null)
                        Toast.makeText(this.context, "Something bad happened",
                                Toast.LENGTH_SHORT).show();
                    else
                        fragmentListener.runUserWs();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LogInDialog", "signInWithEmail:failure", task.getException());
                    Toast.makeText(this.context, "Bad credentials!",
                            Toast.LENGTH_SHORT).show();
                }
            });
    }

    public interface LogInFragmentListener {
        // Run the user Workspace

        void runUserWs();
    }
}
