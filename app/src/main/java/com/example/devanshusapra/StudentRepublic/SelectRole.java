package com.example.devanshusapra.StudentRepublic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SelectRole extends AppCompatActivity {

    private Button cr_btn, student_btn;
    FirebaseAuth.AuthStateListener mAuthListener;
    String role;

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
                        +FirebaseAuth.getInstance().getCurrentUser().getUid()+"/");
        final DatabaseReference childref = mRootref.child("role");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String UserId = user.getUid();
                    childref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot Snapshot) {
                            for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
                                 role = dataSnapshot.getValue(String.class);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if (role.equals("Student")) {
                        startActivity(new Intent(SelectRole.this, StudentActivity.class));
                    }
                    if (role.equals("CR")) {
                        startActivity(new Intent(SelectRole.this, Crpage.class));
                    } else {
                        Toast.makeText(SelectRole.this, "Role Not Found", Toast.LENGTH_SHORT).show();

                    }
                }
            };
        };

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
