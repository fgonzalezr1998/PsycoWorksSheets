package com.example.psycoworksheets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends Activity implements DatabaseHandler.ValueListener.UserListener{

    private FirebaseAuth mAuth;
    private DatabaseHandler.User selectedUser = null;

    // This attributes have to be static because it is used by an inner class!
    private static TextView profileNameText;
    private static String table;

    private ImageView profileAvatar;

    @Override
    public void setUser(DatabaseHandler.User user) {
        this.selectedUser = user;
        if (selectedUser == null) {
            Toast.makeText(this, "Bad credentials!", Toast.LENGTH_LONG).show();
            mAuth.signOut();
            this.finish();
        } else {
            String str = "";
            if (table.equals("Doctors")) {
                str = "Dr. ";
            }
            str += selectedUser.getName();
            profileNameText.setText(str);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        // Initialize attributes

        mAuth = FirebaseAuth.getInstance();
        profileNameText = (TextView) findViewById(R.id.profileNameText);
        profileAvatar = (ImageView) findViewById(R.id.profileImage);

        profileAvatar.setImageResource(R.drawable.empty_profile);

        initParams();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("Profile", "Destroyed!");
        mAuth.signOut();
    }

    private void initParams() {
        // Get Activity input arguments

        Intent intent = getIntent();
        Bundle args = intent.getExtras();

        table = args.getString("table");

        // Get the current user data

        String uid = mAuth.getCurrentUser().getUid();
        if (uid == null)
            return;
        DatabaseHandler.getInstance(this).getUserById(table, uid);  // Get data from database
    }
}
