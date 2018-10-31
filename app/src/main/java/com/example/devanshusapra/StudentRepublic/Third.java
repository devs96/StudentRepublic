package com.example.devanshusapra.StudentRepublic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Third extends AppCompatActivity {

    private Button cr_btn, student_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        cr_btn = findViewById(R.id.cr_btn);
        student_btn = findViewById(R.id.student_btn);

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
                            Toast.makeText(Third.this, "Data could not be saved. "
                                    + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("Data could not be saved. "
                                    + databaseError.getMessage());
                        } else {
                            Toast.makeText(Third.this,
                                    "Added Successfully", Toast.LENGTH_SHORT).show();
                            System.out.println("Data saved successfully.");
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
                            Toast.makeText(Third.this,
                                    "Data could not be saved. "
                                    + databaseError.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            System.out.println("Data could not be saved. "
                                    +  databaseError.getMessage());
                        } else {
                            Toast.makeText(Third.this,
                                    "Added Successfully",
                                    Toast.LENGTH_SHORT).show();
                            System.out.println("Data saved successfully.");
                        }
                    }
                });

            }
        });
    }
}
