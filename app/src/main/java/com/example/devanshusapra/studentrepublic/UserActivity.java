package com.example.devanshusapra.studentrepublic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2);
        Button Student = (Button) findViewById(R.id.button);
        Button CR = (Button) findViewById(R.id.button3);

    }
}
