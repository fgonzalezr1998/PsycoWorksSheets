package com.example.psycoworksheets;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseHandler {
    // DatabaseHandler CLASS IMPLEMENTS A SINGLETON!

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

    static class ValueListener implements ValueEventListener {

        private User user = null;
        private UserListener userListener;

        public ValueListener(Context context) {
            userListener = (UserListener) context;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            user = dataSnapshot.getValue(User.class);
            userListener.setUser(user);
            // ..
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("Profile", "loadPost:onCancelled", databaseError.toException());
        }

        public interface UserListener {
            // Run the user Workspace
            void setUser(User user);
        }
    }

    private static DatabaseHandler databaseHandler = null;
    private DatabaseReference mDatabase;
    private Context context;

    public static DatabaseHandler getInstance(Context context) {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler(context);
        }
        return databaseHandler;
    }

    private DatabaseHandler(Context context) {
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void getUserById(String table, String uid) {
        mDatabase.child("Users").child(table).child(uid).getRef().addValueEventListener(new ValueListener(context));
    }
}
