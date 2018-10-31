package com.example.devanshusapra.StudentRepublic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StudentActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    List <StudentDetails> list = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent();
        String Class_name = intent.getStringExtra("cName");

        recyclerView = findViewById(R.id.recyclerV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentActivity.this));
        progressDialog = new ProgressDialog(StudentActivity.this);
        progressDialog.setMessage("Loading Data From Database");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("notification/" + Class_name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                for (DataSnapshot dataSnapshot : Snapshot.getChildren() ){
                    StudentDetails studentDetails = dataSnapshot.getValue(StudentDetails.class);
                    list.add(studentDetails);
                }
                adapter = new MyAdapter(StudentActivity.this,list);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}

