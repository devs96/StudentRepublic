package com.example.devanshusapra.StudentRepublic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class adminpage extends AppCompatActivity {

    private EditText classvalue, classcode;
    private Button createbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRootref = database.getReference("classes");

        classvalue = findViewById(R.id.e1);
        classcode = findViewById(R.id.editText2);
        createbtn = findViewById(R.id.cr_btn);

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cvalue = classvalue.getText().toString();
                String ccode = classcode.getText().toString();

                DatabaseReference childref = mRootref.child(cvalue);
                childref.setValue(ccode, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           @NonNull DatabaseReference databaseReference) {

                        if (databaseError != null) {
                            Toast.makeText(adminpage.this, "Data could not be saved. "
                                    + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("Data could not be saved. "
                                    + databaseError.getMessage());
                        } else {
                            Toast.makeText(adminpage.this,
                                    "Added Successfully", Toast.LENGTH_SHORT).show();
                            System.out.println("Data saved successfully.");
                        }
                    }
                });
            }
        });
    }
}










