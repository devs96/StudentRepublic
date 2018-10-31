package com.example.devanshusapra.StudentRepublic;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class UserActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2);
        Button Student = (Button) findViewById(R.id.cr_btn);
        Button CR = (Button) findViewById(R.id.student_btn);

    }
}
