package com.example.psycoworksheets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SigninDialogFragment.SignInFragmentListener {

    private RadioButton radioButtonDoctor, radioButtonPatient;
    private Button loginButton, patientSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initParams();
    }

    @Override
    public void onClick(View v) {
        if (noRolSelected()) {
            Toast.makeText(
                    getApplicationContext(),
                    "Please, select one rol", Toast.LENGTH_SHORT).show();
        } else {
            if (v.getId() == R.id.loginButton) {
                startLoginFragment();
            } else {
                startSigninFragment();
            }
        }
    }

    @Override
    public void runSuccessfullySigned(String userName) {
        SuccessfullySignedDialogFragment successfullySigned = new SuccessfullySignedDialogFragment();

        Bundle args = new Bundle();
        args.putString("user_name", userName);

        successfullySigned.setArguments(args);
        successfullySigned.show(getSupportFragmentManager(), "succesfully_signed_fragment");
    }

    private boolean noRolSelected() {
        return !(radioButtonPatient.isChecked() || radioButtonDoctor.isChecked());
    }

    private void startLoginFragment() {
        LogInDialogFragment logInDialogFragment = new LogInDialogFragment();
        logInDialogFragment.show(getSupportFragmentManager(), "login_fragment");
    }

    private void startSigninFragment() {
        SigninDialogFragment signInDialogFragment = new SigninDialogFragment();
        Bundle args = new Bundle();
        args.putString("table", getRol() + "s");

        signInDialogFragment.setArguments(args);
        signInDialogFragment.show(getSupportFragmentManager(), "signin_fragment");
    }

    private String getRol() {
        String rol;
        if (radioButtonDoctor.isChecked()) {
            rol = "Doctor";
        } else {
            rol = "Patient";
        }

        return rol;
    }

    private void initParams() {
        radioButtonDoctor = (RadioButton) findViewById(R.id.radioButtonDoctor);
        radioButtonPatient = (RadioButton) findViewById(R.id.radioButtonPatient);

        loginButton = (Button) findViewById(R.id.loginButton);
        patientSignInButton = (Button) findViewById(R.id.patientSignInButton);
        loginButton.setOnClickListener(this);
        patientSignInButton.setOnClickListener(this);
    }
}