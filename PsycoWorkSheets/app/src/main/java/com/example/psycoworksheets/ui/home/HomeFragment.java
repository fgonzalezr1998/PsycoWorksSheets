package com.example.psycoworksheets.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.psycoworksheets.DatabaseHandler;
import com.example.psycoworksheets.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ValueEventListener {

    private HomeViewModel homeViewModel;
    private static TextView textView1;
    private ListView patientsListView;
    private ArrayList<String> patientsList;
    private View root;
    private DatabaseReference mDatabase;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        textView1 = root.findViewById(R.id.text_home1);

        initParams();

        return root;
    }

    private void initParams() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        textView1 = root.findViewById(R.id.text_home1);
        patientsListView = (ListView) root.findViewById(R.id.patientsListView);
        patientsList = new ArrayList<>();

        composePatientsList();

        textView1.setText("Patients:");
    }

    private void composePatientsList() {
        Query myTopPostsQuery = mDatabase.child("Users").child("Patients");

        // My top posts by number of stars
        myTopPostsQuery.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            DatabaseHandler.User user = postSnapshot.getValue(DatabaseHandler.User.class);
            patientsList.add(user.getName() + " " + user.getSurname() + "\t\t - \t\t" + user.getEmail());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getContext(), R.layout.list_view, R.id.listViewItem, patientsList);

        patientsListView.setAdapter(adapter);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        Log.w("Home Fragment", "loadPost:onCancelled", databaseError.toException());
        // ...
    }
}