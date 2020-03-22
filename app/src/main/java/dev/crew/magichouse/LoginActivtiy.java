package dev.crew.magichouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class LoginActivtiy extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    EditText email,password;
    private FirebaseAuth mAuth;
    TextView forgotPasswordOnClick,singUpOnClick;
    Button loginBtn;
    SignInButton googleSignInBtn ;
    GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activtiy);

        email=findViewById(R.id.edit_email);
        password=findViewById(R.id.edit_password);
        loginBtn=findViewById(R.id.btnLogin);
        forgotPasswordOnClick=findViewById(R.id.forgotPasswordTX);

        singUpOnClick=findViewById(R.id.singUpTX);
        mAuth = FirebaseAuth.getInstance();
        googleSignInBtn = findViewById(R.id.signInButton);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailS = email.getText().toString();
                String passwordS = password.getText().toString();
                if (emailS.isEmpty()) {
                    email.setError("enter email");
                } else if (passwordS.isEmpty()) {
                    password.setError("enter password");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailS).matches()) {
                    password.setError("Enter corect email");
                }else {
                    progressDialog.setMessage("wait...");
                    progressDialog.show();
                    Login( emailS , passwordS );
                }
            }
        });
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });



    }



    private void Login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivtiy.this, MainActivity.class));
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivtiy.this, "Authorization Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivtiy.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void singUp(View view) {
        Intent intent = new Intent(LoginActivtiy.this,SingupActivity.class);
        startActivity(intent);
    }

    public void forgot_password(View view) {
        ShowRecoverPAssword();
    }

    private void ShowRecoverPAssword() {
        progressDialog.setMessage("Sending Email...");
        progressDialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        LinearLayout linearLayout = new LinearLayout(this);
        final EditText editText = new EditText(this);
        editText.setHint("Email");
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        editText.setMaxEms(10);

        linearLayout.addView(editText);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);


        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = editText.getText().toString().intern();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivtiy.this, "Email sent", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivtiy.this, "Failed...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivtiy.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog.dismiss();
            }
        });

        builder.create().show();
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
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google Sign in Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();

                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            // if user login first time get info from email account
                            if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                String user_email = firebaseUser.getEmail();
                                String user_id = firebaseUser.getUid();

                                HashMap<Object , String> hashMap = new HashMap<>();
                                //put info in HashMap

                                hashMap.put("uEmail",user_email);
                                hashMap.put("uId",user_id);
                                hashMap.put("fName","");
                                hashMap.put("lName","");
                                hashMap.put("lPassword","");

                                //firebase database instance
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference("MagicUserData");
                                // put data in hashmap in to database
                                databaseReference.child(user_id).setValue(hashMap);

                            }
                            Toast.makeText(LoginActivtiy.this, ""+firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                            //  updateUI(user);
                            startActivity(new Intent(LoginActivtiy.this, MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            //updateUI(null);
                            Toast.makeText(LoginActivtiy.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivtiy.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
