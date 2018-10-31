package com.example.devanshusapra.StudentRepublic;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class activity_check_code extends AppCompatActivity {

    EditText class_code_field;
    Button CheckBtn, NextBtn;
    TextView confirm_class_name;

    String myClass;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();

        CheckBtn = findViewById(R.id.check_button);
        CheckBtn.setEnabled(false);
        CheckBtn.setVisibility(View.INVISIBLE);
        NextBtn = findViewById(R.id.next_button);
        NextBtn.setVisibility(View.INVISIBLE);
        NextBtn.setEnabled(false);
        confirm_class_name = findViewById(R.id.textView2);
        confirm_class_name.setVisibility(View.INVISIBLE);
        class_code_field = findViewById(R.id.e1);

        class_code_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    CheckBtn.setEnabled(false);
                } else {
                    CheckBtn.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    CheckBtn.setEnabled(false);
                    CheckBtn.setVisibility(View.INVISIBLE);
                } else {
                    CheckBtn.setEnabled(true);
                    CheckBtn.setVisibility(View.VISIBLE);
                }

            }
        });


        CheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("classes").addValueEventListener
                        (new com.google.firebase.database.ValueEventListener() {
                            boolean found=false;
                            @Override
                            public void onDataChange
                                    (com.google.firebase.database.DataSnapshot dataSnapshot) {


                                Iterable<com.google.firebase.database.DataSnapshot> children =
                                        dataSnapshot.getChildren();

                                for (com.google.firebase.database.DataSnapshot child : children) {
                                    if (class_code_field.getText().toString().equals(child.getValue())) {
                                        myClass = child.getKey();
                                        found = true;
                                    }
                                }
                                if (!found) {
                                    NextBtn.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(),
                                            "No Class Found", Toast.LENGTH_SHORT).show();
                                } else {
                                    NextBtn.setEnabled(true);
                                    NextBtn.setVisibility(View.VISIBLE);
                                    confirm_class_name.setVisibility(View.VISIBLE);
                                    confirm_class_name.setText("Class Name:"+myClass);
                                    Toast.makeText(getApplicationContext(),
                                            "Successful", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),
                                        "Error"+databaseError, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    public void OnNextClick(View v) {
        String newDataRef = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase userDB = FirebaseDatabase.getInstance();
        DatabaseReference mRootref = userDB.getReference(
                "users/" + newDataRef);
        DatabaseReference class_key = mRootref.child("class");
        class_key.setValue(myClass);
        Intent intent = new Intent(getApplicationContext(), Crpage.class);
        intent.putExtra("className", myClass);
        intent.putExtra("classCode", class_code_field.getText().toString());
        startActivity(intent);
    }
}