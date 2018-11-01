package com.example.devanshusapra.StudentRepublic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
//        final String ClassName = intent.getStringExtra("className");
//        Log.d("Class Name:",ClassName);
//        String ClassName;

        recyclerView = findViewById(R.id.recyclerV);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lLm = new LinearLayoutManager(StudentActivity.this);
        lLm.setReverseLayout(true);
        lLm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lLm);

        mProgressDialog = new ProgressDialog(StudentActivity.this);
        mProgressDialog.setMessage("Loading Data From Database");
        mProgressDialog.show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRootref =
                database.getReference("users/"
                        +FirebaseAuth.getInstance().getCurrentUser().getUid()+"/");
        final DatabaseReference childref = mRootref.child("class/");
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            childref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot Snapshot) {
                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                        ClassName = dataSnapshot.getValue(String.class);
                        Toast.makeText(StudentActivity.this, "Class :" + ClassName, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            Update();
        }
//        try {
//            loadModelWithDataFromFirebase();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }
    public void Update() {
        databaseReference = FirebaseDatabase.getInstance().getReference("notification/" + ClassName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                    StudentDetails studentDetails = dataSnapshot.getValue(StudentDetails.class);
                    list.add(studentDetails);
                }
                adapter = new MyAdapter(StudentActivity.this, list);
                recyclerView.setAdapter(adapter);
                mProgressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.dismiss();
            }
        });
    }
//    public void loadModelWithDataFromFirebase() throws InterruptedException {
//        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String Uid = currentFirebaseUser.getUid();
//        db = FirebaseDatabase.getInstance().getReference("user/" + Uid);
//        Semaphore semaphore = new Semaphore(0);
//        databaseReference.child("class").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ClassName = dataSnapshot.getValue(String.class);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });
//        semaphore.acquire();
//    }
}
