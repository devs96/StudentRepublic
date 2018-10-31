package com.example.devanshusapra.StudentRepublic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CreateAccount extends AppCompatActivity {

    SignInButton Gbutton;
    GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 2;
    FirebaseAuth.AuthStateListener mAuthListener;
    TextInputLayout firstNameLayout,LastNameLayout,EmailLayout,passLayout,confirmLayout;
    TextInputEditText FirstNameField,LastNameField,passField,confirmField;
    AutoCompleteTextView EmailField;
    MaterialButton CreateAccountBtn;
    FirebaseAuth mAuth;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        FirstNameField = findViewById(R.id.firstName_field);
        LastNameField = findViewById(R.id.lastName_field);
        EmailField = findViewById(R.id.email_field);
        passField = findViewById(R.id.pass_field);
        confirmField = findViewById(R.id.confirm_pass_field);
        CreateAccountBtn = findViewById(R.id.CreateAccountBtn);
        Gbutton = findViewById(R.id.googleBtn) ;
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
                if(firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(
                            CreateAccount.this,Second.class));
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {Toast.makeText(CreateAccount.this,"Google Sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(), null);
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

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure",
                                    task.getException());
                            Toast.makeText(CreateAccount.this,
                                    "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void GooglesignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    private boolean validateForm() {
        boolean valid = true;

        String fnameStr = FirstNameField.getText().toString();
        String lnameStr = LastNameField.getText().toString();
        String emailStr = EmailField.getText().toString();
        String passStr = passField.getText().toString();
        String confirmStr = confirmField.getText().toString();

        if (TextUtils.isEmpty(fnameStr)){
            FirstNameField.setError("Required");
            valid = false;
        }

        if (TextUtils.isEmpty(lnameStr)){
            LastNameField.setError("Required");
            valid = false;
        }

        if (TextUtils.isEmpty(passStr)){
            passField.setError("Required");
            valid = false;
        }
        if (TextUtils.isEmpty(confirmStr)){
            confirmField.setError("Required");
            valid = false;
        }

        if (!passStr.contentEquals(confirmStr)){
            confirmField.setError("Passwords Do No Match");
            valid = false;
        }

        if (TextUtils.isEmpty(emailStr)) {
            EmailField.setError("Required.");
            valid = false;
        } else {
            EmailField.setError(null);
        }

        if (TextUtils.isEmpty(passStr)) {
            passField.setError("Required.");
            valid = false;
        } else {
            passField.setError(null);
        }
        return valid;
    }

    public void tap(View view) {
        if (!validateForm()){
            Toast.makeText(CreateAccount.this,
                    "Validation is False", Toast.LENGTH_SHORT).show();
        }
        createAccount(
                LastNameField.getText().toString(),
                FirstNameField.getText().toString(),
                EmailField.getText().toString(),
                passField.getText().toString()
        );
    }


    private void createAccount(final String first_name, final String last_name, final String email, String password) {
        if (!validateForm()) {return;}

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String newDataRef = FirebaseAuth.getInstance().
                                    getCurrentUser().getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference mRootref = database.getReference(
                                    "users/"+newDataRef);
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference name_key,email_key;

                            name_key = mRootref.child("name");
                            email_key = mRootref.child("email");
                            name_key.setValue(first_name+" "+last_name);
                            email_key.setValue(email);

                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            }
                            catch(FirebaseAuthWeakPasswordException e){Toast.makeText(CreateAccount.this, "Weak Password",Toast.LENGTH_SHORT).show();}
                            catch(FirebaseAuthInvalidCredentialsException e){Toast.makeText(CreateAccount.this, "Invalid Email",Toast.LENGTH_SHORT).show(); }
                            catch(FirebaseAuthUserCollisionException e){Toast.makeText(CreateAccount.this, "User Already Exist",Toast.LENGTH_SHORT).show(); }
                            catch(Exception e){Toast.makeText(CreateAccount.this, e.getMessage(),Toast.LENGTH_SHORT).show();}
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CreateAccount.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Intent createAccountIntent = new Intent(getApplicationContext(),Second.class);
        createAccountIntent.putExtra("firstName",first_name);
        createAccountIntent.putExtra("lastName",last_name);
        createAccountIntent.putExtra("email",email);
        createAccountIntent.putExtra("password",password);
        startActivity(createAccountIntent);
    }

    public void addUserDetails(){

    }
}