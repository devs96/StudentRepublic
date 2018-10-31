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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class Second extends AppCompatActivity {

    private static final String TAG = "Second Activity" ;
    EditText email,fullname,phoneNo;
    FirebaseAuth mAuth;
    Button btn,token_btn;
    Spinner mySpinner;
    DatabaseReference mRootref;
    EditText notf_title_f,notf_desc_f;
    String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        token_btn = findViewById(R.id.token_btn);
        notf_title_f = findViewById(R.id.notf_title);
        notf_desc_f = findViewById(R.id.notf_desc);

        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        Button subscribeButton = findViewById(R.id.subscribe);

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Subscribing to weather topic");
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("weather")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String weather_msg = getString(R.string.msg_subscribed);
                                if (!task.isSuccessful()) {
                                    weather_msg = getString(R.string.msg_subscribe_failed);
                                }
                                Log.d(TAG, weather_msg);
                                Toast.makeText(Second.this, weather_msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                // [END subscribe_topics]
            }
        });

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




    public void sendFCM(View v){

        AtomicInteger msgId = new AtomicInteger();
        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        fm.send(new RemoteMessage.Builder(R.string.sender_id + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .addData("my_message", "Hello World")
                .addData("my_action","SAY_HELLO")
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
                        notf_title_f.setText(msg);
                        Toast.makeText(Second.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void insertNotf(View view) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


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

    private void updateUI(FirebaseUser currentuser) { }

    public void ConfirmBtn(View view) {
        Intent intn = new Intent(this,UserActivity.class);
        startActivity(intn);
    }

    public void SignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Second.this, MainActivity.class));
    }
}
