package com.example.psycoworksheets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends Activity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String table;

    public static class User {
        public String email;
        public String name;
        public String surname;

        public User() {
            // Empty constructor
        }

        public User(String email, String name, String surname) {
            this.email = email;
            this.name = name;
            this.surname = surname;
        }

        public String getEmail() {
            return email;
        }
        public String getName() {
            return name;
        }
        public String getSurname() {
            return surname;
        }
    }

    class ValueListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            User user = dataSnapshot.getValue(User.class);
            Log.w("Profile", user.getName());
            // ..
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initParams();
    }

    private void initParams() {
        // Get Activity input arguments

        Intent intent = getIntent();
        Bundle args = intent.getExtras();

        table = args.getString("table");

        String uid = mAuth.getCurrentUser().getUid();

        // I need to access to the DB and get the fields of the user

        mDatabase.child("Users").child(table).child(uid).getRef().addValueEventListener(new ValueListener());
    }
}
