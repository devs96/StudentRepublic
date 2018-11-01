package com.example.devanshusapra.StudentRepublic;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    SignInButton Gbutton;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 2;
    FirebaseAuth.AuthStateListener mAuthListener;
    String personName, personGivenName, personFamilyName, personEmail, personId;
    Uri personPhoto;
    TextInputLayout layout_email_field,layout_pass_field;
    AutoCompleteTextView email_field;
    TextInputEditText password_field;
    MaterialButton signInBtn,createBtn;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layout_email_field = findViewById(R.id.email_layout);
        layout_pass_field = findViewById(R.id.pass_layout);
        email_field = findViewById(R.id.email_field);
        password_field = findViewById(R.id.pass_field);
        signInBtn = findViewById(R.id.signInBtn);
        createBtn = findViewById(R.id.crtBtn);
        Gbutton = findViewById(R.id.googleBtn) ;


       //layout_email_field.setError("Invalid Email");
       //layout_pass_field.setError("Invalid Password");

        //String email_str = email_field.getText().toString();
        //String pass_str = password_field.getText().toString();

        email_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    layout_email_field.setHint("John.Doe@somaiya.edu");
                }else{
                    layout_email_field.setHint("Email");
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        Gbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GooglesignIn();
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(Login.this,ConfirmDetails.class));
                }
            }
        };
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    private void signIn(String email, String password) {
        if (!validateForm()) { return; }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {Toast.makeText(Login.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        if (!task.isSuccessful()) {Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void GooglesignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {Toast.makeText(Login.this,"Google Sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            String newDataRef = FirebaseAuth.getInstance().
                                    getCurrentUser().getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference mRootref = database.getReference(
                                    "users/"+newDataRef);
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference name_key,email_key;

                            name_key = mRootref.child("name");
                            email_key = mRootref.child("email");
                            name_key.setValue(user.getDisplayName());
                            email_key.setValue(user.getEmail());
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(Login.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                   }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String emailStr = email_field.getText().toString();
        if (TextUtils.isEmpty(emailStr)) {
            email_field.setError("Required.");
            valid = false;
        } else {
            email_field.setError(null);
        }

        String passwrd = password_field.getText().toString();
        if (TextUtils.isEmpty(passwrd)) {
            password_field.setError("Required.");
            valid = false;
        } else {
            password_field.setError(null);
        }
        return valid;
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            Intent myintent = new Intent(this, ConfirmDetails.class);

            myintent.putExtra("firstname", personName);
            myintent.putExtra("mail", personEmail);
            startActivity(myintent);
        }
    }

    public void SignWithEmailPass(View view) {
        signIn(email_field.getText().toString(), password_field.getText().toString());
    }

    public void crtAcnt(View view) {
        String email;
        Intent LoginIntent = new Intent(this, CreateAccount.class);
        if (!TextUtils.isEmpty(email_field.getText())) {
            email = email_field.getText().toString();
            LoginIntent.putExtra("email", email);
        }
        startActivity(LoginIntent);
    }

    public void addUserDetails(){
        Student MyStud = new Student();
        MyStud.setEmail(email_field.getText().toString());
    }
}