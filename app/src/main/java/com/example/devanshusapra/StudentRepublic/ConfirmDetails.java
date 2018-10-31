package com.example.devanshusapra.StudentRepublic;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfirmDetails extends AppCompatActivity {

    private static final String TAG = "ConfirmDetails Activity";

    FirebaseAuth mAuth;
    MaterialButton confirm;
    TextInputLayout name_layout, email_layout;
    DatabaseReference mRootref;
    TextInputEditText name_field, email_field;
    String className;
    Student MyStud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent details = getIntent();
        String name_str = (details.getStringExtra("firstName") + " "
                + details.getStringExtra("lastName"));
        String email_str = details.getStringExtra("email");

        setContentView(R.layout.activity_second);
        name_field = findViewById(R.id.user_name);
        email_field = findViewById(R.id.user_email);

        name_field.setText(name_str);
        email_field.setText(email_str);


//        subscribeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "Subscribing to weather topic");
//                // [START subscribe_topics]
//                FirebaseMessaging.getInstance().subscribeToTopic("weather")
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                String weather_msg = getString(R.string.msg_subscribed);
//                                if (!task.isSuccessful()) {
//                                    weather_msg = getString(R.string.msg_subscribe_failed);
//                                }
//                                Log.d(TAG, weather_msg);
//                                Toast.makeText(ConfirmDetails.this, weather_msg, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                // [END subscribe_topics]
//            }
//        });


    }


    public void sendFCM(View v) {

        AtomicInteger msgId = new AtomicInteger();
        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        fm.send(new RemoteMessage.Builder(R.string.sender_id + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .addData("my_message", "Hello World")
                .addData("my_action", "SAY_HELLO")
                .build());
    }

    public void geneRateToken(View v) {
        // Get token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        name_field.setText(msg);
                        Toast.makeText(ConfirmDetails.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void insertNotf(View view) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        String title_str = name_field.getText().toString();
        String desc_str = email_field.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRootref = database.getReference(
                "notification/" + getClassName());

        DatabaseReference push = mRootref.push();
        DatabaseReference title, desc, time;

        title = push.child("title");
        desc = push.child("message");
        time = push.child("timestamp");
        title.setValue(title_str);
        desc.setValue(desc_str);
        time.setValue(currentDateTimeString);
    }

    public String getClassName() {
        final String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (UID.equals(snapshot.getKey())) {
                                className = snapshot.child("class").getValue().toString();
                                Toast.makeText(getApplicationContext(), className, Toast.LENGTH_LONG).show();
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

        final String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRootref = database.getReference("users/" + UID);

        DatabaseReference name, email;
        name = mRootref.child("name");
        email = mRootref.child("email");
        name.setValue(name_field.getText().toString());
        email.setValue(email_field.getText().toString());
        Intent intn = new Intent(this, SelectClass.class);
        startActivity(intn);
    }

    public void SignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ConfirmDetails.this, Login.class));
    }

    public void addUserDetails() {
        MyStud.setName(name_field.getText().toString());
        MyStud.setEmail(email_field.getText().toString());
    }
}