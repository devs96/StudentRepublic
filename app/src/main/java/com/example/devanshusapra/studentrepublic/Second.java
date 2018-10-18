package com.example.devanshusapra.studentrepublic;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Second extends AppCompatActivity {

    EditText email,fullname, phoneNo;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
//        Intent myintent = getIntent();
//        String mail = myintent.getStringExtra("mail");
//        String fName = myintent.getStringExtra("name");
//
//        email = (EditText) findViewById(R.id.editText);
//        email.setText(mail);
//
//        fullname = (EditText) findViewById(R.id.editText2);
//        fullname.setText(fName);
//
//        if(fName=="")
//        {
//            Toast.makeText(Second.this,"Admin", Toast.LENGTH_SHORT).show();
//        }
       // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseUser user = mAuth.getCurrentUser();
//        String email_str = user.getEmail();
//
//        email.setText(email_str);

//        String name_str = user.getDisplayName();
//        fullname.setText(name_str);
    }





    private void updateUI(FirebaseUser currentuser) {
        //TODO:Update UI with user
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
