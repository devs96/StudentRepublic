package com.example.devanshusapra.StudentRepublic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SelectRole extends AppCompatActivity {

    private Button cr_btn, student_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);

        Intent intent = getIntent();
        final String ClassName = intent.getStringExtra("className");
        final String ClassCode = intent.getStringExtra("classCode");

        student_btn = findViewById(R.id.student_button);
        cr_btn = findViewById(R.id.cr_button);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRootref =
                database.getReference("users/"
                        +FirebaseAuth.getInstance().getUid()+"/");
        final DatabaseReference childref = mRootref.child("role");


        cr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childref.setValue("CR", new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(SelectRole.this, "Data could not be saved. "
                                    + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("Data could not be saved. "
                                    + databaseError.getMessage());
                        } else {
                            Toast.makeText(SelectRole.this,
                                    "Role : CR", Toast.LENGTH_SHORT).show();
                            System.out.println("Data saved successfully.");
                            Intent classes = new Intent(SelectRole.this, Crpage.class);
                            classes.putExtra("className",ClassName);
                            classes.putExtra("classCode",ClassCode);
                            startActivity(classes);

                        }
                    }
                });

            }
        });

        student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childref.setValue("Student",
                        new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(SelectRole.this,
                                    "Data could not be saved. "
                                    + databaseError.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            System.out.println("Data could not be saved. "
                                    +  databaseError.getMessage());
                        } else {
                            Toast.makeText(SelectRole.this,
                                    "Role : Student",
                                    Toast.LENGTH_SHORT).show();
                            System.out.println("Data saved successfully.");
                            Intent classes = new Intent(SelectRole.this, StudentActivity.class);
                            classes.putExtra("className",ClassName);
                            classes.putExtra("classCode",ClassCode);
                            startActivity(classes);

                        }
                    }
                });

            }
        });

    }
}
