package com.example.devanshusapra.StudentRepublic;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.NotificationManager;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.onesignal.OSNotification;
//import com.onesignal.OSNotificationOpenResult;
//import com.onesignal.OneSignal;

import java.text.DateFormat;
import java.util.Date;


public class Crpage extends AppCompatActivity {

    EditText notf_title_f,notf_desc_f;
    Button b1,b2;
    NotificationManager nm;
    String myClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crpage);

        notf_title_f=findViewById(R.id.title_field);
        notf_desc_f=findViewById(R.id.desc_field);
        b1=findViewById(R.id.sednBtn);
        b2=findViewById(R.id.back);

        Intent intent = getIntent();
        final String cName = intent.getStringExtra("className");
        final String cCode = intent.getStringExtra("classCode");

//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.StudentDetails)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();
//        OneSignal.sendTag("user name", cCode);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                notf_title_f = findViewById(R.id.title_field);
                notf_desc_f = findViewById(R.id.desc_field);
                String title_str = notf_title_f.getText().toString();
                String desc_str = notf_desc_f.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mRootref = database
                        .getReference("studentDetails/" + cName);
                DatabaseReference push = mRootref.push();
                DatabaseReference title, desc, time;
                title = push.child("title");
                desc = push.child("message");
                time = push.child("timestamp");
                title.setValue(title_str);
                desc.setValue(desc_str);
                time.setValue(currentDateTimeString);

                StudentDetails studentDetails = new StudentDetails();
                studentDetails.setTitle(title.toString());
                studentDetails.setMessage(desc.toString());
                studentDetails.setTimestamp(time.toString());

                /*Sending Push StudentDetails*/
//
//                DatabaseReference databaseReference = database.getReference();
//                databaseReference.child("studentDetails").addValueEventListener
//                        (new com.google.firebase.database.ValueEventListener() {
//                    @Override
//                    public void onDataChange(com.google.firebase.database
//                                                     .DataSnapshot dataSnapshot) {
//                        Iterable<com.google.firebase.database
//                                .DataSnapshot> children = dataSnapshot.getChildren();
//
//                        for(com.google.firebase.database.DataSnapshot child: children){
//                            if(cName.equals(child.getValue())){
//                                String ns = Context.NOTIFICATION_SERVICE;
//                                NotificationManager nm = (NotificationManager)
//                                        getApplicationContext().getSystemService(ns);
//                                nm.cancelAll();
//                            }
//                        }
//
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
                Intent intent = new Intent(getApplicationContext(),
                        StudentActivity.class);
                intent.putExtra("cName",cName);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        SelectClass.class));
            }
        });
    }
}
