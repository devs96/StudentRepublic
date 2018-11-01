package com.example.devanshusapra.StudentRepublic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class StudentActivity extends AppCompatActivity {

    DatabaseReference databaseReference,db;
    ProgressDialog mProgressDialog;
    List <StudentDetails> list = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    FirebaseUser fuser;
    String ClassName;
    Student MyStud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent();
        final String ClassName = intent.getStringExtra("className");
        Log.d("Class Name:",ClassName);


//        try {
//            loadModelWithDataFromFirebase();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        recyclerView = findViewById(R.id.recyclerV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentActivity.this));


        databaseReference = FirebaseDatabase.getInstance().getReference("notification/"+ClassName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                for (DataSnapshot dataSnapshot : Snapshot.getChildren() ){
                    StudentDetails studentDetails = dataSnapshot.getValue(StudentDetails.class);
                    list.add(studentDetails);
                }
                adapter = new MyAdapter(StudentActivity.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.dismiss();
            }
        });
    }
    public void loadModelWithDataFromFirebase() throws InterruptedException {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String Uid = currentFirebaseUser.getUid();
        db = FirebaseDatabase.getInstance().getReference("user/" + Uid);
        Semaphore semaphore = new Semaphore(0);
        databaseReference.child("class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClassName = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        semaphore.acquire();
    }
}
