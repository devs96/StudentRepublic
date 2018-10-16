package com.example.devanshusapra.studentrepublic;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class Second extends AppCompatActivity {

    EditText email,fullname, phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent myintent = getIntent();
        String mail = myintent.getStringExtra("mail");
        String fName = myintent.getStringExtra("name");
<<<<<<< HEAD
        String phone = myintent.getStringExtra("phone");
=======
>>>>>>> 65c634787b3aad3c8c0ab4ed7e9c7b83734862e2

        email = (EditText) findViewById(R.id.editText);
        email.setText(mail);

        fullname = (EditText) findViewById(R.id.editText2);
        fullname.setText(fName);

<<<<<<< HEAD
        /* TODO: Phone Number */
=======
        if(fName=="")
        {
            Toast.makeText(Second.this,"Admin", Toast.LENGTH_SHORT).show();
        }

        //startActivity(new Intent(Second.this,Second.class));
>>>>>>> 65c634787b3aad3c8c0ab4ed7e9c7b83734862e2

        phoneNo = (EditText) findViewById(R.id.editText5);
        phoneNo.setText(phone);





    }

    public void ConfirmBtn(View view) {
        Intent intn = new Intent(this,UserActivity.class);
        startActivity(intn);
    }
}
