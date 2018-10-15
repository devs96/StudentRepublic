package com.example.devanshusapra.studentrepublic;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class Second extends AppCompatActivity {

    EditText email,fullname,myid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent myintent = getIntent();
        String mail = myintent.getStringExtra("mail");
        String fName = myintent.getStringExtra("name");
        String yourid = myintent.getStringExtra("yourid");

        email = (EditText) findViewById(R.id.editText);
        email.setText(mail);

        fullname = (EditText) findViewById(R.id.editText2);
        fullname.setText(fName);

        myid = (EditText) findViewById(R.id.editText3);
        myid.setText(yourid);

    }
}
