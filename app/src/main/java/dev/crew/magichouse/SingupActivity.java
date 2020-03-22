package dev.crew.magichouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SingupActivity extends AppCompatActivity {
    EditText fName,lName,mail,password,passwordRetype;
    Button creatAccount;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);


        fName=findViewById(R.id.fNameTV);
        lName=findViewById(R.id.lName);
        mail=findViewById(R.id.mailTX);
        password=findViewById(R.id.passwordTX);
        passwordRetype=findViewById(R.id.retypePasswrd);
        creatAccount=findViewById(R.id.createAcountBtn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("wait...");
        mAuth = FirebaseAuth.getInstance();


        creatAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                final String password1 = password.getText().toString();
                String password2 = passwordRetype.getText().toString();
                final String fNameS = fName.getText().toString();
                final String lNameS = lName.getText().toString();


                if (email.isEmpty()) {
                    mail.setError("Enter email");
                }else if (password1.isEmpty()) {
                    password.setError("Enter Password");
                }else if (fNameS.isEmpty()) {
                    fName.setError("Enter First Name");
                }else if (lNameS.isEmpty()) {
                    lName.setError("Enter Last Name");
                }else if (password2.isEmpty()) {
                    passwordRetype.setError("Enter Password");
                }else if (!password1.equals(password2)) {
                    passwordRetype.setError("Password not match");
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
                    mail.setError("Enter DOB");
                }else if(password.length()<6){
                    passwordRetype.setError("Password length");
                    password.setError("Password length");
                }else {
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email,password1)
                            .addOnCompleteListener(SingupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SingupActivity.this, "Sign Up", Toast.LENGTH_SHORT).show();
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                        String user_email = firebaseUser.getEmail();
                                        String user_id = firebaseUser.getUid();

                                        HashMap<Object , String> hashMap = new HashMap<>();
                                        //put info in HashMap


                                        hashMap.put("uEmail",user_email);
                                        hashMap.put("uId",user_id);
                                        hashMap.put("fName",fNameS);
                                        hashMap.put("lName",lNameS);
                                        hashMap.put("lPassword",password1);

                                        //firebase database instance
                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference databaseReference = firebaseDatabase.getReference("MagicUserData");

                                        // put data in hashmap in to database
                                        databaseReference.child(user_id).setValue(hashMap);

                                        startActivity(new Intent(SingupActivity.this,LoginActivtiy.class));
                                        finish();
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SingupActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(SingupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }


            }
        });


    }
}
