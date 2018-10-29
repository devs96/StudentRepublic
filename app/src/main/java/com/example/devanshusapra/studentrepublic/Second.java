package com.example.devanshusapra.studentrepublic;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Second extends AppCompatActivity {

    EditText email,fullname,phoneNo;
    FirebaseAuth mAuth;
    Button btn;
    Spinner mySpinner;
    DatabaseReference mRootref;
    EditText notf_title_f,notf_desc_f;
    String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);



//        String name_str = (details.getStringExtra("firstName")+" "
//                        +details.getStringExtra("lastName"));
//        String email_str = details.getStringExtra("email");
//        String password_str = details.getStringExtra("password");

//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        rootRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Is better to use a List, because you don't know the size
//                // of the iterator returned by dataSnapshot.getChildren() to
//                // initialize the array
//                final List<String> classes = new ArrayList<String>();
//
//                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
//                    String classNames = areaSnapshot.child("classes").getKey();
//                    classes.add(classNames);
//                }
//
//                Spinner mySpinner = (Spinner) findViewById(R.id.myspinner);
//                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Second.this, android.R.layout.simple_spinner_item, classes);
//                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mySpinner.setAdapter(areasAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        String newDataRef = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference mRootref = database.getReference(
//                "users/"+newDataRef);

//        Intent userdetails = getIntent();
//        String fname = userdetails.getStringExtra("firstName");
//        String lname = userdetails.getStringExtra("lastName");
//        String email = userdetails.getStringExtra("email");
//        String password = userdetails.getStringExtra("password");
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference name_key,email_key,class_key;
//
//        name_key = mRootref.child("name");
//        name_key.setValue(user.getDisplayName());
//
//        email_key = mRootref.child("email");
//        email_key.setValue(user.getEmail());
//
//        class_key = mRootref.child("class");
//        class_key.setValue("mca");
    }

    public void insertNotf(View view) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        notf_title_f = findViewById(R.id.notf_title);
        notf_desc_f = findViewById(R.id.notf_desc);


        String title_str = notf_title_f.getText().toString();
        String desc_str = notf_desc_f.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRootref = database.getReference(
                "notification/"+getClassName());

        DatabaseReference push = mRootref.push();
        DatabaseReference title,desc,time;

        title = push.child("title");
        desc = push.child("message");
        time = push.child("timestamp");
        title.setValue(title_str);
        desc.setValue(desc_str);
        time.setValue(currentDateTimeString);
    }

    public String getClassName(){
        final String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (UID.equals(snapshot.getKey())){
                                className = snapshot.child("class").getValue().toString();
                                Toast.makeText(getApplicationContext(),className,Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        return className;
    }

    private void updateUI(FirebaseUser currentuser) {
    }

    public void ConfirmBtn(View view) {
        Intent intn = new Intent(this,UserActivity.class);
        startActivity(intn);
    }

    public void SignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Second.this, MainActivity.class));
    }
}
