package com.example.devanshusapra.studentrepublic;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
